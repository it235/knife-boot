package com.it235.knife.es.entity;

import com.it235.knife.es.metadata.EsDocument;
import com.it235.knife.es.metadata.EsField;
import com.it235.knife.es.metadata.EsSource;
import com.it235.knife.es.metadata.EsTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 23:39
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
@ApiModel("搜索引擎 商品信息Doc")
@EsSource(index = EsGoods.indexName)
public class EsGoods extends EsDocument {

    public static final String indexName = "tf_goods";

    @EsField(type = EsTypeEnum.KEYWORD)
    private Long goodsId;

    @EsField()
    private Integer goodsType;

    @EsField(type = EsTypeEnum.KEYWORD)
    private String goodsName;

    @EsField()
    private BigDecimal price;

    @EsField()
    private String desc;

    @EsField()
    private Integer status;

    @EsField(type = EsTypeEnum.KEYWORD)
    private Integer creatorId;

    @EsField()
    private Date createTime;

}
