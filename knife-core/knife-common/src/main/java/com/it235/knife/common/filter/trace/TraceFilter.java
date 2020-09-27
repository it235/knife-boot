package com.it235.knife.common.filter.trace;

/**
 * @description: TraceFilter基类 主要分为spring-web和spring-webflux 2种写法
 * @author: jianjun.ren
 * @date: Created in 2020/9/26 22:52
 */
public interface TraceFilter {
    void init0();
}
