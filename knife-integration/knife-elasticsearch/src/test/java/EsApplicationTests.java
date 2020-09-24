
import com.it235.knife.es.EsApplication;
import com.it235.knife.es.entity.EsGoods;
import com.it235.knife.es.support.ESQuerySupport;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 23:45
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class EsApplicationTests {

    @Autowired
    private ESQuerySupport esService;
//
//    @Test
//    public void testAll() throws InterruptedException {
//        t1AddOne();
//        t2AddBatch();
//        Thread.sleep(1000);
//        t3FindAll();
//        t4search();
//        t5deleteOne();
//        t6deleteBatch();
//        Thread.sleep(1000);
//        t7FindAll();
//
//    }
    @Test
    public void testAll() throws Exception {
        wildcardQuery();

    }
    /**
     * 检查商品是否存在
     * @return
     * @throws IOException
     */
    public boolean wildcardQuery() {
        String goodsName = "深入理解JVM*";
        //注意keyword类型的需要加上.keyword，若要模糊需要再value上加上通配符
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("goods_name.keyword", goodsName);
        List<EsGoods> list = null;
        try {
            //列出15条
            list = esService.getDocuments(queryBuilder, 15, EsGoods.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println(list);
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

//    @Test
//    public void t1AddOne() {
//        IndexResponse putOne = eSNService.putOne(new Book(1, "西游记"));
//        System.out.println("【1】putOne：" + putOne);
//    }
//
//    @Test
//    public void t2AddBatch() {
//        List<EsGoods> list = new ArrayList<>();
//        list.add(new EsGoods(2, "水浒传"));
//        list.add(new EsGoods(3, "三国演义"));
//        BulkResponse putBatch = bookService.putBatch(list);
//        System.out.println("【2】putBatch：" + putBatch.status());
//    }
//
//    @Test
//    public void t3FindAll() {
//        System.out.println("【3】");
//        List<EsGoods> res = bookService.getAll();
//        System.out.println("↓↓↓findAll");
//        res.forEach(System.out::println);
//        System.out.println("↑↑↑findAll");
//    }
//
//    @Test
//    public void t4search() {
//        System.out.println("【4】");
//        List<EsGoods> searchByKey = bookService.searchByKey("水传");
//        searchByKey.forEach(System.out::println);
//
//        EsGoods book = bookService.getByBookId(2);
//        System.out.println("【4】getByBookId：" + book);
//    }
//
//    @Test
//    public void t5deleteOne() {
//        BulkByScrollResponse deleteById = bookService.deleteById(1);
//        System.out.println("【5】deleteById：" + deleteById.getStatus());
//    }
//
//    @Test
//    public void t6deleteBatch() {
//        List<Integer> ids = new ArrayList<>();
//        ids.add(2);
//        ids.add(3);
//        BulkResponse deleteBatch = bookService.deleteBatch(ids);
//        System.out.println("【6】deleteBatch：" + deleteBatch.status());
//    }
//
//    @Test
//    public void t7FindAll() {
//        System.out.println("【7】");
//        List<EsGoods> res = bookService.getAll();
//        System.out.println("↓↓↓findAll");
//        res.forEach(System.out::println);
//        System.out.println("↑↑↑findAll");
//    }
}