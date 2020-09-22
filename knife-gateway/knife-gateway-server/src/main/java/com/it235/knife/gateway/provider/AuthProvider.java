package com.it235.knife.gateway.provider;

import com.it235.knife.gateway.constants.GatewayConstant;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Ron
 * @CreateTime 2020/9/21 22:28
 */
public class AuthProvider {
    public static String TARGET = "/**";
    public static String REPLACEMENT = "";
    public static String AUTH_KEY = GatewayConstant.HEADER;
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private static List<String> defaultSkipTokenUrl = new ArrayList<>();

    static {
        defaultSkipTokenUrl.add("/example");
        defaultSkipTokenUrl.add("/token/**");
        defaultSkipTokenUrl.add("/captcha/**");
        defaultSkipTokenUrl.add("/actuator/health/**");
        defaultSkipTokenUrl.add("/v2/api-docs/**");
        defaultSkipTokenUrl.add("/v2/api-docs-ext/**");
        defaultSkipTokenUrl.add("/auth/**");
        defaultSkipTokenUrl.add("/oauth/**");
        defaultSkipTokenUrl.add("/log/**");
        defaultSkipTokenUrl.add("/menu/routes");
        defaultSkipTokenUrl.add("/menu/auth-routes");
        defaultSkipTokenUrl.add("/tenant/info");
        defaultSkipTokenUrl.add("/order/create/**");
        defaultSkipTokenUrl.add("/storage/deduct/**");
        defaultSkipTokenUrl.add("/error/**");
        defaultSkipTokenUrl.add("/assets/**");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getDefaultSkipUrl() {
        return defaultSkipTokenUrl;
    }

}
