package com.it235.knife.common.constants;

/**
 * @description: 多租户所使用的常量
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 22:14
 */
public interface TenantConstant {
    /**
     * 当租户id以header的形式传进来时的属性名
     */
    String TENANT_ID_HEADER_PROP = "x-tenant-header";

    /**
     * 当租户id以param参数传进来时的属性名
     */
    String TENANT_ID_PARAM_PROP = "tenantId";

    /**
     * 系统默认的租户ID
     * 所有表结构数据默认 10000
     */
    String DEFAULT_TENANT_ID = "10000";
}
