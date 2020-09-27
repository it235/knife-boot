package com.it235.knife.feign;

import cn.hutool.core.util.StrUtil;
import com.it235.knife.common.constants.GlobalConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/26 23:30
 */
@Slf4j
@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                //待实现其他功能

            }
        }
        //补充traceId
        String traceId = MDC.get(GlobalConstant.LOG_TRACE_ID);
        if (StrUtil.isBlank(traceId)) {
            traceId = TraceContext.traceId();
            if (StrUtil.isBlank(traceId)) {
                template.header(GlobalConstant.TRACE_ID_HEADER, TraceContext.traceId());
            }
        }
    }
}
