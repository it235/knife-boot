package com.it235.knife.service.impl;

import com.it235.knife.goods.api.GoodsApi;
import com.it235.knife.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 12:05
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GoodsApi goodsApi;

    public String get(String orderNum) {
        String goods = goodsApi.get(123);
        return "订单商品：" + goods;
    }
}
