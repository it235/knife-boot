package com.it235.knife.gateway.filter;

import cn.hutool.core.util.IdUtil;
import com.it235.knife.common.constants.GlobalConstant;
import com.it235.knife.common.filter.trace.TraceFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description: 链路追踪过滤器
 * @author: jianjun.ren
 * @date: Created in 2020/9/25 14:40
 */
@Slf4j
@Component
public class GateWayTraceFilter implements TraceFilter , GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //像日志中写入链路追踪id
//        String traceId = IdUtil.fastSimpleUUID();
        String traceId = TraceContext.traceId();
        log.info("链路ID：{}" , traceId);
        MDC.put(GlobalConstant.LOG_TRACE_ID, traceId);
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(h -> h.add(GlobalConstant.TRACE_ID_HEADER, traceId))
                .build();

        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void init0() {
        log.info("开始加载GateWay中的TraceFilter！");
    }
}
