package com.it235.knife.data.mybatis.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 11:36
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "knife.tenant")
public class TenantProperties {
    /**
     * 是否开启租户模式
     */
    private Boolean enable = false;

    /**
     * 需要排除的多租户的表
     */
    private List<String> ignoreTables = Arrays.asList("knife_sys_user", "knife_sys_dict", "knife_sys_depart",
            "knife_sys_role", "knife_sys_tenant", "knife_sys_role_permission");

    /**
     * 多租户字段名称
     */
    private String column = "tenant_id";

    /**
     * 排除不进行租户隔离的sql
     * 样例全路径：com.it235.knife.sys.mapper.UserMapper.list
     */
    private List<String> ignoreSqls = new ArrayList<>();
}
