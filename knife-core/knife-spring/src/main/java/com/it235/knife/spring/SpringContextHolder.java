package com.it235.knife.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 17:48
 */
public class SpringContextHolder {
    private static ApplicationContext applicationContext;
    private static ApplicationContext parentApplicationContext;

    public SpringContextHolder() {
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public SpringContextHolder(ApplicationContext ctx) {
        Assert.notNull(ctx, "SpringContextHolder injection ApplicationContext is null");
        applicationContext = ctx;
        parentApplicationContext = ctx.getParent();
    }

    public static Object getBean(String name) {
        Assert.hasText(name, "SpringContextHolder name is null or empty");

        try {
            return applicationContext.getBean(name);
        } catch (Exception var2) {
            return parentApplicationContext.getBean(name);
        }
    }

    public static <T> T getBean(String name, Class<T> type) {
        Assert.hasText(name, "SpringContextHolder name is null or empty");
        Assert.notNull(type, "SpringContextHolder type is null");

        try {
            return applicationContext.getBean(name, type);
        } catch (Exception var3) {
            return parentApplicationContext.getBean(name, type);
        }
    }

    public static <T> T getBean(Class<T> type) {
        Assert.notNull(type, "SpringContextHolder type is null");

        try {
            return applicationContext.getBean(type);
        } catch (Exception var2) {
            return parentApplicationContext.getBean(type);
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        Assert.notNull(type, "SpringContextHolder type is null");

        try {
            return applicationContext.getBeansOfType(type);
        } catch (Exception var2) {
            return parentApplicationContext.getBeansOfType(type);
        }
    }

    public static ApplicationContext publishEvent(Object event) {
        applicationContext.publishEvent(event);
        return applicationContext;
    }
}
