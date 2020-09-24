//package com.it235.knife.es.support;
//
///**
// * @description:
// * @author: jianjun.ren
// * @date: Created in 2020/9/22 23:44
// */
//import com.it235.knife.es.EsUtil;
//import com.it235.knife.es.entity.EsGoods;
//import com.it235.knife.es.util.EsEntity;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.index.query.*;
//import org.elasticsearch.index.reindex.BulkByScrollResponse;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
////@Component
//public class EsService {
//
////    @Autowired
//    private EsUtil esUtil;
//
//    public Boolean indexExists(String index) throws Exception {
//        return esUtil.indexExist(index);
//    }
//    public List<EsGoods> getAll() {
//        return esUtil.search(EsUtil.INDEX_NAME, new SearchSourceBuilder(), EsGoods.class);
//    }
//
//    public EsGoods getByBookId(int bookId) {
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        builder.query(new TermQueryBuilder("bookId", bookId));
//        List<EsGoods> res = esUtil.search(EsUtil.INDEX_NAME, builder, EsGoods.class);
//        if (res.size() > 0) {
//            return res.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    public List<EsGoods> searchByKey(String key) {
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(QueryBuilders.matchQuery("name", key));
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        builder.size(10).query(boolQueryBuilder);
//        return esUtil.search(EsUtil.INDEX_NAME, builder, EsGoods.class);
//    }
//
//    public IndexResponse putOne(EsGoods book) {
//        EsEntity<EsGoods> entity = new EsEntity<>(book.getBookId() + "", book);
//        return esUtil.insertOrUpdateOne(EsUtil.INDEX_NAME, entity);
//    }
//
//    public BulkResponse putBatch(List<EsGoods> books) {
//        List<EsEntity> list = new ArrayList<>();
//        books.forEach(item -> list.add(new EsEntity<>(item.getBookId() + "", item)));
//        return esUtil.insertBatch(EsUtil.INDEX_NAME, list);
//    }
//
//    public BulkByScrollResponse deleteById(int id) {
//        return esUtil.deleteByQuery(EsUtil.INDEX_NAME, new TermQueryBuilder("bookId", id));
//    }
//
//    public BulkResponse deleteBatch(List<Integer> list) {
//        return esUtil.deleteBatch(EsUtil.INDEX_NAME, list);
//    }
//
//}
