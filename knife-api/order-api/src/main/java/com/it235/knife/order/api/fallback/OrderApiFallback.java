package com.it235.knife.order.api.fallback;

import com.it235.knife.order.api.OrderApi;
import org.springframework.stereotype.Component;

/**
 * 订单API接口熔断类
 * @Author Ron
 * @CreateTime 2020/9/22 11:53
 */
@Component
public class OrderApiFallback implements OrderApi {

    public String get(String orderNum) {
        return "访问服务超时，请稍后重试";
    }
}
