package com.it235.knife.log.annotation;

import java.lang.annotation.*;

/**
 * @description: 日志注解类
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 14:55
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    boolean enabled() default true;

    String value() default "";

    boolean controllerApiValue() default true;

    boolean request() default true;

    boolean requestByError() default true;

    boolean response() default true;
}
