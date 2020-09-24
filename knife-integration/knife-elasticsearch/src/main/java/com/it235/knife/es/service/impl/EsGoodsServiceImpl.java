package com.it235.knife.es.service.impl;

import com.it235.knife.es.entity.EsGoods;
import com.it235.knife.es.support.ESQuerySupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 12:25
 */
@Service
@Slf4j
public class EsGoodsServiceImpl {

    @Autowired
    private ESQuerySupport esSupport;

    /**
     * 校验索引是否存在
     */
    public void checkIndexExists(){
        String indexName = EsGoods.indexName;
        try {
            boolean b = esSupport.existIndex(indexName);
            if(b){
                log.info("索引：{} 存在" , indexName);
            }else{
                log.info("索引：{} 不存在" , indexName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加商品到ES中，如果索引不存在，则会创建索引
     * @return
     */
    public boolean addGoodsToES() {
        EsGoods esGoods = EsGoods.of()
                .setGoodsId(123L).setGoodsName("深入理解JVM1")
                .setGoodsType(1).setCreateTime(new Date())
                .setCreatorId(110).setDesc("书本描述")
                .setPrice(BigDecimal.ONE).setStatus(1);
        esGoods.setIndex(EsGoods.indexName);
        IndexResponse indexResponse = null;
        try {
            indexResponse = esSupport.addOrUpdateDocument(esGoods);
        } catch (IOException e) {
            System.out.println("添加索引失败" + indexResponse.getResult().getLowercase());
            e.printStackTrace();
        }
        if(indexResponse.status().getStatus() == 200){
            return true;
        }
        return false;
    }

    /**
     * 根据索引ID修改ES中的信息
     * @return
     */
    public boolean updateGoodsToES() {
        EsGoods esGoods = EsGoods.of()
                .setGoodsId(123L).setGoodsName("深入理解JVM1")
                .setCreateTime(new Date())
                .setCreatorId(110).setDesc("书本描述")
                .setPrice(BigDecimal.ONE).setStatus(1);
        esGoods.setIndex(EsGoods.indexName);
        esGoods.setId("W1InuXQB6W0InXdYFP6t");
        IndexResponse indexResponse = null;
        try {
            indexResponse = esSupport.addOrUpdateDocument(esGoods);
        } catch (IOException e) {
            System.out.println("添加索引失败" + indexResponse.getResult().getLowercase());
            e.printStackTrace();
        }
        if(indexResponse.status().getStatus() == 200){
            return true;
        }
        return false;
    }


    /**
     * 通配符查询
     * 注意：
     *  1.*号不要写在最前面
     *  2.若属性时keyword类型，则需要加上.keyword
     * @return
     * @throws IOException
     */
    public List<EsGoods> wildcardQuery() {
        //通配符参数
        String wildcardParam = "深入理解JVM*";
        //条件属性
        String fieldName = "goods_name.keyword";
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery(fieldName , wildcardParam);
        List<EsGoods> list = new ArrayList<>();
        try {
            //列出15条
            list = esSupport.getDocuments(queryBuilder, 15, EsGoods.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("查询结果：{}", list);
        return list;
    }
    //分页查询
}
