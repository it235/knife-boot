package com.it235.knife.es.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 23:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsEntity<T> {

    private String id;
    private T data;

}
