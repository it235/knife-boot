package com.it235.knife.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it235.knife.common.http.Result;
import com.it235.knife.goods.entity.Goods;

import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 13:44
 */
public interface GoodsService extends IService<Goods> {

    Goods findGoodsById(int id);

    Result<String> findOrderByGoodsId(int id);

    List<Goods> listGoods();

    IPage<Goods> queryPage(Integer pageNo, Integer pageSize);
}
