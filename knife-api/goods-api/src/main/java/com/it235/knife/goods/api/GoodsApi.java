package com.it235.knife.goods.api;

//import com.it235.knife.goods.api.fallback.GoodsApiFallback;
import com.it235.knife.goods.api.fallback.GoodsApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:59
 */
//运行后会变为knife-goods-server或knife-goods-server01，其中01是指01迭代，多迭代分支时可用到
@FeignClient(name = "${knife.feign.goods-server:knife-goods-server}${sprint.branch.version:}"
        ,path = "/goods",fallback = GoodsApiFallback.class)
public interface GoodsApi {

    @GetMapping("/{id}")
    String get(@PathVariable("id") int id);
}
