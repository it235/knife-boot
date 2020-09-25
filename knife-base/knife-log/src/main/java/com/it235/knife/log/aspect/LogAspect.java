package com.it235.knife.log.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.it235.knife.common.constants.GlobalConstant;
import com.it235.knife.common.tl.TenantTreadLocal;
import com.it235.knife.core.http.Result;
import com.it235.knife.log.annotation.Log;
import com.it235.knife.log.entity.OperationLog;
import com.it235.knife.log.event.LogEvent;
import com.it235.knife.log.util.LogUtil;
import com.it235.knife.spring.SpringContextHolder;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 14:48
 */
@Slf4j
@Aspect
public class LogAspect {

    private static final ThreadLocal<OperationLog> OPRATION_TL = new ThreadLocal();
    /**
     * 切入点  所有controller包中的类和方法参数  || 带有@Log注解的方法或属性
     * within(包名.类名)
     * execution(返回值类型 包名.类名.方法名(参数类型,参数类型…))
     *
     */
    @Pointcut("execution(public * com.it235.knife.*.controller.*.*(..)) || @annotation(com.it235.knife.log.annotation.Log)")
    public void log() {
        //该方法声明切入点
        System.out.println("进入切点");
    }

    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("log()")
    public void beforeLog(JoinPoint point) {
        Log logAnnotation = LogUtil.getTargetAnnotation(point);
        boolean check = check(point, logAnnotation);
        if(!check){
            OperationLog opration = this.wrapperOpration();
            String controllerDescription = "";
            Api api = (Api)point.getTarget().getClass().getAnnotation(Api.class);
            if (api != null) {
                String[] tags = api.tags();
                if (tags != null && tags.length > 0) {
                    controllerDescription = tags[0];
                }
            }

            String controllerMethodDescription = LogUtil.getDescribe(logAnnotation);
            Object[] args;
            if (StrUtil.isNotEmpty(controllerMethodDescription) && StrUtil.contains(controllerMethodDescription, "#")) {
                args = point.getArgs();
                MethodSignature methodSignature = (MethodSignature)point.getSignature();
                controllerMethodDescription = this.getValBySpEL(controllerMethodDescription, methodSignature, args);
            }

            if (StrUtil.isEmpty(controllerDescription)) {
                opration.setDescription(controllerMethodDescription);
            } else if (logAnnotation.controllerApiValue()) {
                opration.setDescription(controllerDescription + "-" + controllerMethodDescription);
            } else {
                opration.setDescription(controllerMethodDescription);
            }

            opration.setClassPath(point.getTarget().getClass().getName());
            opration.setActiveMethod(point.getSignature().getName());
            args = point.getArgs();
            HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            if (logAnnotation.request()) {
                String strArgs = this.getArgs(logAnnotation , args, request);
                opration.setParams(this.getText(strArgs));
            }

            //该链路ID值初次在网关TraceFilter中设置
            opration.setTraceId(MDC.get(GlobalConstant.LOG_TRACE_ID));
            if (request != null) {
                opration.setReqIp(ServletUtil.getClientIP(request, new String[0]));
                opration.setReqUri(URLUtil.getPath(request.getRequestURI()));
                opration.setHttpMethod(request.getMethod());
                opration.setUa(StrUtil.sub(request.getHeader(GlobalConstant.USER_AGENT_HEADER), 0, 500));
                opration.setTenantId(TenantTreadLocal.getTenantId());
                if (StrUtil.isEmpty(opration.getTraceId())) {
                    opration.setTraceId(request.getHeader(GlobalConstant.TRACE_ID_HEADER));
                }
            }
            //将信息存起来，在after之后使用
            opration.setStartTime(LocalDateTime.now());
            OPRATION_TL.set(opration);
        }
    }

    /**
     * 后置操作
     */
    @AfterReturning(
            returning = "ret",
            pointcut = "log()"
    )
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        Log sysLogAnno = LogUtil.getTargetAnnotation(joinPoint);
        if (!this.check(joinPoint, sysLogAnno)) {
            Result r = Convert.convert(Result.class, ret);
            OperationLog opration = this.wrapperOpration();
            if (r == null) {
                opration.setType("OPT");
                if (sysLogAnno.response()) {
                    opration.setResult(this.getText(String.valueOf(ret == null ? "" : ret)));
                }
            } else {
                if (r.isSuccess()) {
                    opration.setType("OPT");
                } else {
                    opration.setType("EX");
                    opration.setException(r.getMsg());
                }

                if (sysLogAnno.response()) {
                    opration.setResult(this.getText(r.toString()));
                }
            }

            this.publishEvent(opration);
        }
    }

    /**
     * 异常切入
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "log()", throwing = "e")
    public void sendErrorLog(JoinPoint joinPoint, Exception e) {
        Log logAnnotation = LogUtil.getTargetAnnotation(joinPoint);
        if (!this.check(joinPoint, logAnnotation)) {
            OperationLog operation = this.wrapperOpration();
            operation.setType("EX");
            if (!logAnnotation.request() && logAnnotation.requestByError() && StrUtil.isEmpty(operation.getParams())) {
                Object[] args = joinPoint.getArgs();
                HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                String strArgs = this.getArgs(logAnnotation, args, request);
                operation.setParams(this.getText(strArgs));
            }
            operation.setException(ExceptionUtil.stacktraceToString(e, 65535));
            this.publishEvent(operation);
        }
    }


    private boolean check(JoinPoint joinPoint, Log logAnnotation) {
        if (logAnnotation != null && logAnnotation.enabled()) {
            Log targetClass = (Log)joinPoint.getTarget().getClass().getAnnotation(Log.class);
            return targetClass != null && !targetClass.enabled();
        } else {
            return true;
        }
    }

    private OperationLog wrapperOpration() {
        OperationLog operationLog = OPRATION_TL.get();
        return operationLog == null ? new OperationLog() : operationLog;
    }


    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 解析SPEL
     * @param spEL
     * @param methodSignature
     * @param args
     * @return
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
        try {
            String[] paramNames = this.nameDiscoverer.getParameterNames(methodSignature.getMethod());
            if (paramNames != null && paramNames.length > 0) {
                Expression expression = this.spelExpressionParser.parseExpression(spEL);
                EvaluationContext context = new StandardEvaluationContext();

                for(int i = 0; i < args.length; ++i) {
                    context.setVariable(paramNames[i], args[i]);
                    context.setVariable("p" + i, args[i]);
                }

                return expression.getValue(context).toString();
            }
        } catch (Exception var8) {
            log.warn("解析操作日志的el表达式出错", var8);
        }

        return spEL;
    }


    private String getText(String val) {
        return StrUtil.sub(val, 0, 65535);
    }

    private String getArgs(Log logAnnotation , Object[] args, HttpServletRequest request) {
        String strArgs = "";

        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception var8) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception var7) {
                log.warn("解析参数异常", var7);
            }
        }

        return strArgs;
    }


    private void publishEvent(OperationLog operation) {
        operation.setFinishTime(LocalDateTime.now());
        operation.setConsumingTime(operation.getStartTime().until(operation.getFinishTime(), ChronoUnit.MILLIS));
        SpringContextHolder.publishEvent(new LogEvent(operation));
        OPRATION_TL.remove();
    }

}
