package com.it235.knife.es.support;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 0:15
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.it235.knife.es.metadata.EsDocument;
import com.it235.knife.es.metadata.EsField;
import com.it235.knife.es.metadata.EsSource;
import com.it235.knife.es.metadata.EsTypeEnum;
import com.it235.knife.es.util.EsEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @description: ES工具支持类
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 10:27
 */
@Slf4j
@Component
public class ESQuerySupport {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    static PropertyNamingStrategy strategy = PropertyNamingStrategy.SnakeCase;

    /**
     * 序列化配置
     */
    static SerializeConfig serializeConfig = getSerializeConfig(strategy);

    /**
     * 反序列化配置
     */
    static ParserConfig parseConfig = getParserConfig(strategy);

    /**
     * 序列化配置
     *
     * @return
     */
    private static SerializeConfig getSerializeConfig(PropertyNamingStrategy strategy) {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = strategy;
        return config;
    }

    /**
     * 序列化配置
     * @return
     */
    private static ParserConfig getParserConfig(PropertyNamingStrategy strategy) {
        ParserConfig parseConfig = new ParserConfig();
        parseConfig.propertyNamingStrategy = strategy;
        return parseConfig;
    }

    /**
     * 索引是否存在
     *
     * @param indexName 索引名称
     * @return
     * @throws IOException
     */
    public boolean existIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     *
     * @param indexName        索引名称
     * @param mappingJson      索引映射
     * @param numberOfShards   主分片数量
     * @param numberOfReplicas 副分片数量
     * @return
     * @throws IOException
     */
    public CreateIndexResponse createIndex(String indexName, String mappingJson, Integer numberOfShards,
                                           Integer numberOfReplicas) throws IOException {
        if (existIndex(indexName)) {
            return null;
        }
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        Builder builder = Settings.builder();
        if (numberOfShards != null) {
            builder.put("index.number_of_shards", numberOfReplicas);
        }
        if (numberOfReplicas != null) {
            builder.put("index.number_of_replicas", numberOfReplicas);
        }
        request.settings(builder);
        if (!StringUtils.isEmpty(mappingJson)) {
            request.mapping(mappingJson, XContentType.JSON);
        }
        return restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     * @param clazz
     * @param numberOfShards
     * @param numberOfReplicas
     * @return
     * @throws IOException
     */
    public <T> CreateIndexResponse createIndex(Class<T> clazz,Integer numberOfShards,
                                               Integer numberOfReplicas) throws IOException {
        EsSource indexAnnotation = clazz.getAnnotation(EsSource.class);
        String index = indexAnnotation.index();
        JSONObject jsonObject = new JSONObject();
        JSONObject properties = new JSONObject();
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            EsField esFieldAnnotaion = field.getAnnotation(EsField.class);
            if (esFieldAnnotaion == null) {
                continue;
            }
            JSONObject esFieldDef = new JSONObject();
            if (esFieldAnnotaion.type().equals(EsTypeEnum.AUTO)) {
                esFieldDef.put("type", EsTypeEnum.getEnumByClazz(field.getType()));
            } else {
                esFieldDef.put("type", esFieldAnnotaion.type());
            }
            String name = strategy.translate(field.getName());
            properties.put(name, esFieldDef);
        }
        jsonObject.put("properties", properties);
        String mappingJson = jsonObject.toString();
        return createIndex(index, mappingJson, numberOfShards, numberOfReplicas);
    }

    /**
     * 添加文档
     * @param document
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public <T extends EsDocument> IndexResponse addOrUpdateDocument(T document) throws IOException {
        return addDocument(document.getId() , document.getIndex(), JSON.toJSONString(document, serializeConfig));
    }
        /**
         * 添加文档
         *
         * @param index    索引名称
         * @param jsonData 文档数据
         * @return
         * @throws IOException
         */
    private IndexResponse addDocument(String id , String index, String jsonData) throws IOException {
        IndexRequest request = new IndexRequest(index);
        if(Objects.nonNull(id)){
            request.id(id);
        }
        request.source(jsonData, XContentType.JSON);
        return restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }
    /**
     * 获取类字段
     *
     * @param clazz
     * @return
     */
    public List<Field> getFields(Class<?> clazz) {
        if (Object.class.equals(clazz) || EsDocument.class.equals(clazz)) {
            return new ArrayList<>();
        }
        List<Field> rSet = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            rSet.addAll(Arrays.asList(fields));
        }
        rSet.addAll(getFields(clazz.getSuperclass()));
        return rSet;
    }

    /**
     * 分组查询
     *
     * @param indexName
     * @param queryBuilder
     * @param aggregationBuilder
     * @param size
     * @return
     * @throws IOException
     */
    public SearchResponse aggrDocuments(String indexName, QueryBuilder queryBuilder, QueryBuilder posteQueryBuilder,
                                        AggregationBuilder aggregationBuilder, Integer from, Integer size) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        if (posteQueryBuilder != null) {
            searchSourceBuilder.postFilter(posteQueryBuilder);
        }
        if (from != null) {
            searchSourceBuilder.from(from);
        }
        if (size != null) {
            searchSourceBuilder.size(size);
        }
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 获取数据
     * @param documentId 数据id
     * @throws IOException
     */
    public <T extends EsDocument> T getDocumentById(String documentId, Class<T> clazz) throws IOException {
        EsSource indexAnnotation = clazz.getAnnotation(EsSource.class);
        String index = indexAnnotation.index();
        GetRequest getRequest = new GetRequest(index, documentId);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        if (!getResponse.isExists()) {
            return null;
        }
        String id = getResponse.getId();
        long version = getResponse.getVersion();
        String sourceAsString = getResponse.getSourceAsString();
        T result = JSON.parseObject(sourceAsString, clazz, parseConfig);
        result.setId(id);
        result.setIndex(index);
        result.setVersion(version);
        return result;
    }

    public <T extends EsDocument> List<T> getDocuments(QueryBuilder queryBuilder, Integer size, Class<T> clazz) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        if (size != null) {
            searchSourceBuilder.size(size);
        }
        EsSource indexAnnotation = clazz.getAnnotation(EsSource.class);
        String index = indexAnnotation.index();
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        List<T> ret = new ArrayList<>(hits.getHits().length);
        hits.forEach(item -> {
            String json = item.getSourceAsString();
            T t = JSON.parseObject(json, clazz, parseConfig);
            t.setId(item.getId());
            t.setIndex(item.getIndex());
            t.setVersion(item.getVersion());
            ret.add(t);
        });
        return ret;
    }

    /**
     * 批量插入
     * @param index
     * @param list
     * @return
     * @throws IOException
     */
    public BulkResponse addBatchDocument(String index, List<EsEntity> list) throws IOException {
        BulkRequest request = new BulkRequest();
        for (EsEntity item : list) {
            String _json = JSON.toJSONString(item.getData());
            String _id = item.getId();
            IndexRequest indexRequest = new IndexRequest(index).id(_id).source(_json, XContentType.JSON);
            request.add(indexRequest);
        }
        return restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }


    /**
     * 删除索引
     * @param index
     * @return
     * @throws IOException
     */
    public AcknowledgedResponse deleteIndex(String index) throws IOException {
        IndicesClient indicesClient = restHighLevelClient.indices();
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = indicesClient.delete(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 删除指定索引的查询项
     * @param index
     * @param builder
     * @return
     * @throws IOException
     */
    public BulkByScrollResponse deleteByQuery(String index, QueryBuilder builder) throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(builder);
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 根据ID批量删除索引中的内容
     * @param index
     * @param idList
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> BulkResponse deleteBatch(String index, Collection<T> idList) throws IOException {
        BulkRequest request = new BulkRequest();
        for (T t : idList) {
            request.add(new DeleteRequest(index, t.toString()));
        }
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 分页查询
     * @param list
     * @param current
     * @param size
     * @param <T>
     * @return
     */
    public <T> Page<T> pageList(List<T> list, long current, long size) {
        Page<T> page = new Page<T>();
        page.setCurrent(current);
        page.setSize(size);
        page.setTotal(0);
        List<T> ret = Lists.newArrayList();
        if (list == null || list.isEmpty()) {
            page.setRecords(ret);
            page.setPages(0);
            return page;
        }
        page.setTotal(list.size());
        long start = (current - 1) * size;
        long end = start + size;
        if (end > list.size()) {
            end = list.size();
        }
        for (int i = (int) start; i < end; i++) {
            ret.add(list.get(i));
        }
        long mod = list.size() % size;
        long pages = list.size() / size;
        if (mod == 0) {
            page.setPages(pages);
        } else {
            page.setPages(pages + 1);
        }
        page.setRecords(ret);
        return page;
    }

    public static void main(String[] args) throws Exception {


        // RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
        //         RestClient.builder(
        //                 new HttpHost("192.168.0.12", 9200, "http")));
        // BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        // QueryBuilder mustQuery = QueryBuilders.termQuery("subject", "1");
        // boolBuilder.must(mustQuery);

        // QueryBuilder queryBuilder = QueryBuilders.constantScoreQuery(boolBuilder);
        // SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // searchSourceBuilder.query(queryBuilder);
        // searchSourceBuilder.size(0);
        // SearchRequest searchRequest = new SearchRequest("tf_subject_tag");
        // AggregationBuilder unionIdAgg = AggregationBuilders.terms("group_by_union_id").field("union_id");
        // AggregationBuilder batchIdAgg = AggregationBuilders.terms("group_by_batch_id").field("batch_id");
        // AggregationBuilder topBuilder = AggregationBuilders.topHits("top_record").sort("batch_id", SortOrder.DESC);
        // batchIdAgg.subAggregation(topBuilder);
        // unionIdAgg.subAggregation(batchIdAgg);
        // searchSourceBuilder.aggregation(unionIdAgg);
        // searchRequest.source(searchSourceBuilder);
        // SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // System.out.println(JSON.toJSONString(response));


        // 默认配置
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        // 设置搜索，可以是任何类型的 QueryBuilder
////        sourceBuilder.query(QueryBuilders.termQuery("tagNameList", "帅气"));
//        // 起始 index
////        sourceBuilder.from(0);
//        // 起始 index
////        sourceBuilder.size(5);
//        // 设置搜索的超时时间
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//
//        SearchRequest request = new SearchRequest();
//        // 索引
//        request.indices("subjects_tags_02");
//        request.source(sourceBuilder);
//
////        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("tagNameList", "帅气");
////        matchQueryBuilder.fuzziness(Fuzziness.AUTO);  // 模糊查询
////        matchQueryBuilder.prefixLength(3); // 前缀查询的长度
////        matchQueryBuilder.maxExpansions(10); // max expansion 选项，用来控制模糊查询
////
//
//        MatchPhraseQueryBuilder matchQueryBuilder = new MatchPhraseQueryBuilder("tagNameList", "帅气");
////        matchQueryBuilder.boost()
//
//        sourceBuilder.query(matchQueryBuilder);
//
//
//        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
//        SearchHit[] hits = search.getHits().getHits();
//        for (SearchHit hit : hits){
//            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//            System.out.println(sourceAsMap.get("unionId"));
//
//        }
//
//        System.out.println(search.toString());
//
//        WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("tagNameList", "帅气");

//                        new HttpHost("localhost", 9201, "http")));
//        GetRequest getRequest = new GetRequest("subjects_tags_01");
//        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//        // /ecommerce/product/1
//        if (getResponse.isExists()) {
//            String index = getResponse.getIndex();
//            String type = getResponse.getType();
//            String id = getResponse.getId();
//            long version = getResponse.getVersion();
//            String sourceAsString = getResponse.getSourceAsString(); // string 形式
//            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap(); // map
//            byte[] sourceAsBytes = getResponse.getSourceAsBytes(); // 字节形式
//        } else {
//            // 没有发现请求的文档
//        }
//
//
//        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "kimchy");
//        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
//        matchQueryBuilder.prefixLength(3);
//        matchQueryBuilder.maxExpansions(10);
//
//        QueryBuilder queryBuilder = QueryBuilders.matchQuery("user", "kimchy")
//                .fuzziness(Fuzziness.AUTO)
//                .prefixLength(3)
//                .maxExpansions(10);



//        try {
//            // todo 索引暂时写死 需要维护枚举
//            String index = "subjects_tags_03";
//            String fieldName = "tagNameList";
//            // 默认配置
//            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//            // 超时时间
////                sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//            SearchRequest request = new SearchRequest();
//            // 索引
//            request.indices(index);
//            request.source(sourceBuilder);
//            // todo 还需要通过type来 过滤数据
//            WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder(fieldName, "大");
//            TermQueryBuilder type = new TermQueryBuilder("subjectType", 4);
//
//
//            sourceBuilder.query(boolQueryBuilder);
//            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
//            // 处理查询出数据
//            SearchHits hits = search.getHits();
//
//            System.out.println(search.toString());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(wildcardQueryBuilder);
//        boolQueryBuilder.must(type);

    }


    public SearchResponse getScenesTags(String indexName, int scene) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        SearchRequest searchRequest = new SearchRequest(indexName);
        // 标签名称分组
        AggregationBuilder tagNameAgg = AggregationBuilders.terms("group_by_tag_name").field("tag_name");
        // 使用场景分组
        AggregationBuilder sceneAgg = AggregationBuilders.terms("group_by_scene").field("scene");
        // 取最大值
        AggregationBuilder topTime = AggregationBuilders.topHits("top_time").sort("create_time", SortOrder.DESC);
        tagNameAgg.subAggregation(sceneAgg);
        tagNameAgg.subAggregation(topTime);
        searchSourceBuilder.aggregation(tagNameAgg);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }
}

