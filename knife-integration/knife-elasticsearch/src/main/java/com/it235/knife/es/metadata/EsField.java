package com.it235.knife.es.metadata;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 0:17
 */

import com.it235.knife.es.metadata.EsTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsField {
    EsTypeEnum type() default EsTypeEnum.AUTO;
}