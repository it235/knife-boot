package com.it235.knife.log.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: OperationLog操作日志实体类
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 15:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "OperationLog实体类", description = "普通操作日志封装")
public class OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ip地址")
    private String reqIp;

    @ApiModelProperty(value = "跟踪ID")
    private String traceId;

    @ApiModelProperty(value = "日志类型")
    private String type;

    @ApiModelProperty(value = "操作描述")
    private String description;

    @ApiModelProperty(value = "请求路径")
    private String reqUri;

    @ApiModelProperty(value = "执行方法签名")
    private String activeMethod;

    @ApiModelProperty(value = "请求方式GET/POST")
    private String httpMethod;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "执行结果")
    private String result;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "异常信息")
    private String exception;

    @ApiModelProperty(value = "耗时")
    private Long consumingTime;

    @ApiModelProperty(value = "UserAgent")
    private String ua;

    @ApiModelProperty(value = "执行人ID")
    private Long createUser;

    @ApiModelProperty(value = "执行的类路径")
    private String classPath;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;
}
