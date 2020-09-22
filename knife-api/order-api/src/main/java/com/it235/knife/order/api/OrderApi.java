package com.it235.knife.order.api;

import com.it235.knife.order.api.fallback.OrderApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:51
 */
@FeignClient(name = "${knife.feign.order-server:knife-order-server}${sprint.branch.version:}",
        path = "/order", fallback = OrderApiFallback.class)
public interface OrderApi {

    @GetMapping("/${orderNum}")
    String get(@PathVariable String orderNum);

}
