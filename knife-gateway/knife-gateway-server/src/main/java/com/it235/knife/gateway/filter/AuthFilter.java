package com.it235.knife.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it235.knife.gateway.properties.AuthProperties;
import com.it235.knife.gateway.provider.AuthProvider;
import com.it235.knife.gateway.provider.ResponseProvider;
import com.it235.knife.gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 校验Token信息
 */
@Component
@Slf4j
@AllArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private AuthProperties authProperties;
    private ObjectMapper objectMapper;
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();
        HttpHeaders headers = request.getHeaders();
        String path = request.getURI().getPath();
        if (isSkipUrl(path)) {
            return chain.filter(exchange);
        }
        String headerToken = headers.getFirst(AuthProvider.AUTH_KEY);
        String paramToken = request.getQueryParams().getFirst(AuthProvider.AUTH_KEY);
//        if (StringUtils.isAllBlank(headerToken, paramToken)) {
//            return unAuth(response, "缺失令牌,鉴权失败");
//        }
        String auth = StringUtils.isBlank(headerToken) ? paramToken : headerToken;
        String token = JwtUtil.getToken(auth);
        Claims claims = JwtUtil.parseJWT(token);
//        if (claims == null) {
//            return unAuth(response, "请求未授权");
//        }
        ServerHttpRequest build = mutate.build();
        return chain.filter(exchange.mutate().request(build).build());
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String result = "";
        try {
            result = objectMapper.writeValueAsString(ResponseProvider.unAuth(msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

    public int getOrder() {
        //值越小，优先级越高
        return -99;
    }

    private boolean isSkipUrl(String path) {
        return authProperties.isSkipTokenUrl(path);
    }
}
