package com.it235.knife.service;

import com.it235.knife.goods.dto.GoodsDTO;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 12:05
 */
public interface OrderService {
    GoodsDTO get(String orderNum);
}
