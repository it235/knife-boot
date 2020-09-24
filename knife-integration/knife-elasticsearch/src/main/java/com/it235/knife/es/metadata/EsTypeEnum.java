package com.it235.knife.es.metadata;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 0:18
 */

import java.util.Date;

import lombok.Getter;

@Getter
public enum EsTypeEnum {
    AUTO(""),
    KEYWORD("keyword"),
    TEXT("text"),
    INTEGER("integer"),
    DOUBLE("double"),
    LONG("long"),
    BOOLEAN("boolean"),
    DATE("date");

    private String type;

    EsTypeEnum(String type) {
        this.type = type;
    }

    public static EsTypeEnum getEnumByClazz(Class<?> clazz) {
        if (clazz == null) {
            return AUTO;
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return INTEGER;
        }
        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return DOUBLE;
        }
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return LONG;
        }
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return BOOLEAN;
        }
        if (clazz.equals(Date.class)) {
            return DATE;
        }
        return AUTO;
    }
}