package com.it235.knife.common.tl;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @description: TTL中多租户传递
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 16:40
 */
public class TenantTreadLocal {

    /**
     * 阿里巴巴开源
     * 支持缓存线程的组件情况下传递ThreadLocal
     */
    private static final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

    /**
     * TTL 设置租户ID<br/>
     * <b>谨慎使用此方法,避免嵌套调用 </b>
     * @param tenantId 租户ID
     */
    public static void setTenantId(String tenantId) {
        THREAD_LOCAL_TENANT.set(tenantId);
    }

    /**
     * 获取TTL中的租户ID
     * @return String
     */
    public static String getTenantId() {
        return THREAD_LOCAL_TENANT.get();
    }

    /**
     * 清除tenantId
     */
    public static void clear() {
        THREAD_LOCAL_TENANT.remove();
    }
}
