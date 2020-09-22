package com.it235.knife.gateway.constants;

/**
 * @Author Ron
 * @CreateTime 2020/9/21 22:10
 */
public interface GatewayConstant {
    String KNIFE_VERSION = "1.0.0";

    String SKIP_TOKEN_PREFIX = "skip.token";
    String SWAGGER_DOCUMENT_PREFIX = "swagger.document";

    String SIGN_KEY = "knife";
    String AVATAR = "avatar";
    String HEADER = "knife-auth";
    String BEARER = "bearer";
    String ACCESS_TOKEN = "access_token";
    String REFRESH_TOKEN = "refresh_token";
    String TOKEN_TYPE = "token_type";
    String EXPIRES_IN = "expires_in";
    String ACCOUNT = "account";
    String USER_ID = "user_id";
    String ROLE_ID = "role_id";
    String USER_NAME = "user_name";
    String ROLE_NAME = "role_name";
    String TENANT_ID = "tenant_id";
    String OAUTH_ID = "oauth_id";
    String CLIENT_ID = "client_id";
    String LICENSE = "license";
    String LICENSE_NAME = "powered by knife";
    String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";
    Integer AUTH_LENGTH = 7;
}
