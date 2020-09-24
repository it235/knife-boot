package com.it235.knife.goods.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it235.knife.core.http.Result;
import com.it235.knife.goods.entity.Goods;
import com.it235.knife.goods.service.GoodsService;
import com.it235.knife.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:44
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 根据ID获取商品信息
     * @param goodsId
     * @return
     */
    @Log
    @GetMapping("{id}")
    public Result<Goods> get(@PathVariable("id") Integer goodsId){
        Goods goods = goodsService.findGoodsById(goodsId);

        return Result.ofSuccess(goods);
    }

    /**
     * 获取所有的商品集合
     * @return
     */
    @GetMapping("list")
    public Result<List<Goods>> list(){
        List<Goods> goodsList = goodsService.listGoods();
        return Result.ofSuccess(goodsList);
    }
    @Log
    @GetMapping("pages")
    public Result<IPage<Goods>> pages(Integer pageNo, Integer pageSize){
        IPage<Goods> goodsIPage = goodsService.queryPage(pageNo, pageSize);
        return Result.ofSuccess(goodsIPage);
    }
}
