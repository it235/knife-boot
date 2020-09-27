package com.it235.knife.data.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @description: 实体基类，不可随意改动
 * @author: jianjun.ren
 * @date: Created in 2020/10/1 0:08
 */
@Data
public class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private String createBy;

    @TableField
    private Date createTime;

    @TableField
    private String updateBy;

    @TableField
    private Date updateTime;

    @TableField
    private Integer delFlag;

    @TableField
    private String deleteBy;

    @TableField
    private Date deleteTime;

    @TableField
    private String tenantId;
}