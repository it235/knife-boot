package com.it235.knife.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it235.knife.common.exception.BizException;
import com.it235.knife.common.http.Result;
import com.it235.knife.common.http.ResultCode;
import com.it235.knife.goods.entity.Goods;
import com.it235.knife.goods.mapper.GoodsMapper;
import com.it235.knife.goods.service.GoodsService;
import com.it235.knife.order.api.OrderFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 商品Service
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 13:44
 */
@Slf4j
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {



    @Autowired
    private OrderFeignApi orderFeignApi;

    @Autowired
    private GoodsMapper goodsMapper;

    public Goods findGoodsById(int id) {
        Goods goods = goodsMapper.findGoodsById(id);
        if(goods == null){
            throw new BizException(ResultCode.FAILURE.getCode() , "未获取到商品数据");
        }
        return goods;
    }

    @Override
    public Result<String> findOrderByGoodsId(int id) {
        return orderFeignApi.get(id+"");
    }

    public List<Goods> listGoods() {
        List<Goods> goods = goodsMapper.listGoods();
        return goods;
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    public IPage<Goods> queryPage(Integer pageNo, Integer pageSize) {
        IPage<Goods> page = new Page<>(pageNo, pageSize);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        IPage<Goods> goodsPage = baseMapper.selectPage(page, wrapper);
        return goodsPage;
    }

}
