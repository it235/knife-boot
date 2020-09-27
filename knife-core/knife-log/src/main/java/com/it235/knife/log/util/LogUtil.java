package com.it235.knife.log.util;

import com.it235.knife.log.annotation.Log;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 15:11
 */
@Slf4j
@NoArgsConstructor
public class LogUtil {

    public static String getDescribe(JoinPoint point) {
        Log annotation = getTargetAnnotation(point);
        return annotation == null ? "" : annotation.value();
    }

    public static String getDescribe(Log annotation) {
        return annotation == null ? "" : annotation.value();
    }

    public static Log getTargetAnnotation(JoinPoint point) {
        try {
            Log annotation = null;
            if (point.getSignature() instanceof MethodSignature) {
                Method method = ((MethodSignature)point.getSignature()).getMethod();
                if (method != null) {
                    annotation = (Log)method.getAnnotation(Log.class);
                }
            }
            return annotation;
        } catch (Exception var3) {
            log.warn("获取 {}.{} 的 @Log 注解失败", new Object[]{point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), var3});
            return null;
        }
    }
}
