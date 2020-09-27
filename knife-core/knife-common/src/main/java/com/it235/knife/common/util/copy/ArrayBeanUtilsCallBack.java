package com.it235.knife.common.util.copy;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/25 10:17
 */
@FunctionalInterface
public interface ArrayBeanUtilsCallBack<S, T> {

    void callBack(S t, T s);
}