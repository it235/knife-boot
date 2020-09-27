package com.it235.knife.order.api;

import com.it235.knife.common.http.Result;
import com.it235.knife.order.api.fallback.OrderFeignApiFallback;
import com.it235.knife.order.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:51
 */
@FeignClient(name = "${knife.api.order-server:knife-order-server}${sprint.branch.version:}",
        fallback = OrderFeignApiFallback.class)
public interface OrderFeignApi {

    @GetMapping("/order/{orderNum}")
    Result<String> get(@PathVariable("orderNum") String orderNum);

}
