package com.it235.knife.gateway.swagger;

import com.it235.knife.gateway.properties.AuthProperties;
import com.it235.knife.gateway.swagger.handler.HystrixFallbackHandler;
import com.it235.knife.gateway.swagger.handler.SwaggerResourceHandler;
import com.it235.knife.gateway.swagger.handler.SwaggerSecurityHandler;
import com.it235.knife.gateway.swagger.handler.SwaggerUiHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * Swagger配置，将swagger的3个方法注册到路由上，并提供相应的处理器进行处理
 * @Author Ron
 * @CreateTime 2020/9/22 9:49
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class SwaggerFunctionHandler {

    private final SwaggerResourceHandler swaggerResourceHandler;
    private final SwaggerSecurityHandler swaggerSecurityHandler;
    private final SwaggerUiHandler swaggerUiHandler;
    private final HystrixFallbackHandler hystrixFallbackHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/swagger-resources")
                .and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)

                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)

                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler)

                .andRoute(RequestPredicates.GET("/fallback")
                        .and(RequestPredicates.accept(MediaType.ALL)), hystrixFallbackHandler);
    }
}
