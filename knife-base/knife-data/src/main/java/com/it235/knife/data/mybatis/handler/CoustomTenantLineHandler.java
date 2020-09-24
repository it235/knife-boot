package com.it235.knife.data.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.it235.knife.common.tl.TenantTreadLocal;
import com.it235.knife.data.mybatis.props.TenantProperties;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;

import java.util.List;

/**
 * @description: 多租户实现
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 16:29
 */
@AllArgsConstructor
public class CoustomTenantLineHandler implements TenantLineHandler {

    private TenantProperties tenantProperties;

    /**
     * 获取租户ID
     * @return
     */
    @Override
    public Expression getTenantId() {
        String tenant = TenantTreadLocal.getTenantId();
        if (tenant != null) {
            return new StringValue(TenantTreadLocal.getTenantId());
        }
        return new NullValue();
    }

    /**
     * 租户ID字段名，可以在TenantProperties中修改
     * @return
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

    /**
     * 过滤不需要根据租户隔离的表
     * 默认返回 false 表示所有表都需要拼多租户条件
     * 可以在TenantProperties中修改
     * @param tableName 表名
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return tenantProperties.getIgnoreTables().stream().anyMatch(
                (t) -> t.equalsIgnoreCase(tableName));
    }
}
