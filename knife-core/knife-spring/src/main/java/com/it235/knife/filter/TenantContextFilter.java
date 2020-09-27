package com.it235.knife.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.it235.knife.common.constants.TenantConstant;
import com.it235.knife.common.tl.TenantTreadLocal;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 租户拦截器，各微服务需要集成，由网关转发进来
 *
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 22:12
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextFilter extends GenericFilterBean {

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            //优先取请求参数中的tenantId值
            String tenantId = request.getParameter(TenantConstant.TENANT_ID_PARAM_PROP);
            if (ObjectUtil.isEmpty(tenantId)) {
                tenantId = request.getHeader(TenantConstant.TENANT_ID_HEADER_PROP);
            }
            log.debug("获取到的租户ID为:{}", tenantId);
            if (StrUtil.isNotBlank(tenantId)) {
                TenantTreadLocal.setTenantId(tenantId);
            } else {
                if (StrUtil.isBlank(TenantTreadLocal.getTenantId())) {
                    TenantTreadLocal.setTenantId(TenantConstant.DEFAULT_TENANT_ID);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantTreadLocal.clear();
        }
    }
}
