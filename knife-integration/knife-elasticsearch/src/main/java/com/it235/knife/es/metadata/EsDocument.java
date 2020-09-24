package com.it235.knife.es.metadata;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 0:17
 */

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
public abstract class EsDocument {
    /**
     * 索引名称
     */
    @JSONField(serialize = false, deserialize = false)
    private String index;

    /**
     * 文档id
     */
    @JSONField(serialize = false, deserialize = false)
    private String id;

    /**
     * 版本号
     */
    @JSONField(serialize = false, deserialize = false)
    private long version;

}