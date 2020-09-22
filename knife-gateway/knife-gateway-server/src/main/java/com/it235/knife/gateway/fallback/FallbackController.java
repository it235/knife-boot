package com.it235.knife.gateway.fallback;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 响应超时熔断处理器
 *
 * @author zuihou
 */
//@RestController
//public class FallbackController {
//
//    @RequestMapping("/fallback")
//    public Mono<String> fallback() {
//        return Mono.just("请求服务失败");
//    }
//}
