package com.it235.knife.gateway.properties;

import com.it235.knife.gateway.constants.GatewayConstant;
import com.it235.knife.gateway.provider.AuthProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;


/**
 * 跳过授权的处理
 * @Author Ron
 * @CreateTime 2020/9/21 22:10
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = GatewayConstant.SKIP_TOKEN_PREFIX)
public class AuthProperties {
    /**
     * 无需token的API集合
     */
    private List<String> url = new ArrayList<>();


    /**
     * 判断当前URI是否跳过权限校验
     */
    public Boolean isSkipTokenUrl(String path) {
        return AuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains)
                || getUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::contains);
    }
}
