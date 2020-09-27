package com.it235.knife.goods.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it235.knife.common.util.copy.ArrayBeanUtils;
import com.it235.knife.common.http.Result;
import com.it235.knife.goods.api.GoodsFeignApi;
import com.it235.knife.goods.dto.GoodsDTO;
import com.it235.knife.goods.entity.Goods;
import com.it235.knife.goods.service.GoodsService;
import com.it235.knife.log.annotation.Log;
import com.it235.knife.redis.config.FastJsonRedisConfig;
import com.it235.knife.redis.manager.RedisManager;
//import com.it235.knife.redis.util.RedisBaseUtil;
//import com.it235.knife.redis.util.RedisListUtil;
//import com.it235.knife.redis.util.RedisValUtil;
import com.it235.knife.redis.util.RedisBaseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
//    @Autowired
//    private RedisManager string3RedisManager;
//    @Autowired
//    private RedisManager string4RedisManager;
    @Autowired
    private RedisBaseUtil redisBaseUtil;
//    @Autowired
//    private RedisValUtil redisValUtil;
//    @Autowired
//    private RedisListUtil redisListUtil;

    /**
     * 根据ID获取商品信息
     * @param goodsId
     * @return
     */
    @Log
    @ApiOperation(value = "根据商品ID获取商品信息")
    public Result<GoodsDTO> get(@PathVariable("id") Integer goodsId){
        List<GoodsDTO> list = new ArrayList<>();
        GoodsDTO goodsDTO1 = new GoodsDTO();
        goodsDTO1.setName("zhagnsan");
        goodsDTO1.setType(123);
//        redisBaseUtil.set("ABC" , 888);
        long abc1 = redisBaseUtil.getExpire("ABC");
        redisBaseUtil.delete(0,"ABC");
        System.out.println(abc1);
        boolean abc = redisBaseUtil.hasKey(1,"abc");
        System.out.println(abc);
        redisBaseUtil.delete(1,"lis");
//        string3RedisManager.set("ABC","123");
        GoodsDTO goodsDTO12 = new GoodsDTO();
        goodsDTO12.setName("lisi");
        goodsDTO12.setType(456);
//        string4RedisManager.set("DEF","456");
//        GoodsDTO[] array = new GoodsDTO[2];
//        array[0] = goodsDTO1;
//        array[1] = goodsDTO12;
//        list.add(goodsDTO1);
//        list.add(goodsDTO12);
//        redisValUtil.set("abc" ,goodsDTO12 );
//        GoodsDTO goodsDTO2 = redisValUtil.get("abc", GoodsDTO.class);
//        System.out.println("key是否存在：" +goodsDTO2);
//        redisListUtil.insert("lis" , array);
//        List<GoodsDTO> lis = redisListUtil.getListAll("lis", GoodsDTO.class);
//        System.out.println("lis:->" + lis);
        return Result.ofSuccess(goodsDTO1);
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
