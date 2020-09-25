package com.it235.knife.goods.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.it235.knife.data.mybatis.entity.BaseEntity;
import lombok.Data;

/**
 * @description: DTO实体，接口调用及Controller统一用DTO
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 14:23
 */
@Data
public class GoodsDTO extends BaseEntity {

    @TableField
    private String name;

    @TableField("type")
    private Integer type;
}
