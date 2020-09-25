package com.it235.knife.goods.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it235.knife.common.util.copy.ArrayBeanUtils;
import com.it235.knife.core.http.Result;
import com.it235.knife.goods.api.GoodsFeignApi;
import com.it235.knife.goods.dto.GoodsDTO;
import com.it235.knife.goods.entity.Goods;
import com.it235.knife.goods.service.GoodsService;
import com.it235.knife.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:44
 */
@Api(value = "商品（goods）相关接口")
@RestController
public class GoodsController implements GoodsFeignApi {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据ID获取商品信息
     * @param goodsId
     * @return
     */
    @Log
    @ApiOperation(value = "根据商品ID获取商品信息")
    public Result<GoodsDTO> get(@PathVariable("id") Integer goodsId){
        goodsService.findOrderByGoodsId(1);
        Goods goods = goodsService.findGoodsById(goodsId);
        GoodsDTO goodsDTO = new GoodsDTO();
        BeanUtils.copyProperties(goods,goodsDTO);
        return Result.ofSuccess(goodsDTO);
    }

    /**
     * 获取所有的商品集合
     * @return
     */
    @ApiOperation(value = "查询商品列表")
    public Result<List<GoodsDTO>> list(){
        List<Goods> goodsList = goodsService.listGoods();
        List<GoodsDTO> goodsDTOS = ArrayBeanUtils.copyListProperties(goodsList, GoodsDTO::new);
        return Result.ofSuccess(goodsDTOS);
    }

    @Log
    @ApiOperation(value = "分页查询商品列表")
    public Result<IPage<GoodsDTO>> pages(Integer pageNo, Integer pageSize){
        return null;
//        IPage<Goods> goodsIPage = goodsService.queryPage(pageNo, pageSize);
//        return Result.ofSuccess(goodsIPage);
    }
}
