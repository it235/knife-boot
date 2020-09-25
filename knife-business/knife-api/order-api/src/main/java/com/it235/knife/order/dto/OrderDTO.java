package com.it235.knife.order.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.it235.knife.data.mybatis.entity.BaseEntity;
import lombok.Data;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/25 12:43
 */
@Data
public class OrderDTO extends BaseEntity {

    @TableField
    private String name;

    @TableField("type")
    private Integer type;
}
