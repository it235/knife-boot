package com.it235.knife.goods.api.fallback;

import com.it235.knife.goods.api.GoodsApi;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Component;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:59
 */
@Component
public class GoodsApiFallback implements GoodsApi {
    public String get(int id) {
        return "请求GoodsApi超时";
    }
}
