package com.it235.knife.filter;

import cn.hutool.core.util.StrUtil;
import com.it235.knife.common.constants.GlobalConstant;
import com.it235.knife.common.filter.trace.TraceFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/26 22:57
 */
@Slf4j
@ConditionalOnClass(Filter.class)
public class WebTraceFilter extends OncePerRequestFilter implements TraceFilter {

    public WebTraceFilter(){
        init0();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String traceId = request.getParameter(GlobalConstant.TRACE_ID_PARAM);
            if (StrUtil.isBlank(traceId)) {
                traceId = request.getHeader(GlobalConstant.TRACE_ID_HEADER);
                if (StrUtil.isBlank(traceId)) {
                    traceId = TraceContext.traceId();
                }
            }
            MDC.put(GlobalConstant.LOG_TRACE_ID, traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void init0() {
        log.info("WEB中TraceFilter加载");
    }
}
