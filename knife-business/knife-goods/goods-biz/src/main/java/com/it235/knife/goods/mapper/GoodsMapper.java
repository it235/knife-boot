package com.it235.knife.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it235.knife.goods.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 13:49
 */
@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from goods where id = #{id}")
    Goods findGoodsById(int id);

    List<Goods> listGoods();

}
