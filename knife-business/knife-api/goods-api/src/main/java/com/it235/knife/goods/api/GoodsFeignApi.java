package com.it235.knife.goods.api;

//import GoodsApiFallback;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it235.knife.common.http.Result;
import com.it235.knife.goods.dto.GoodsDTO;
import com.it235.knife.goods.api.fallback.GoodsApiFallbackApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign层，继承API模块，也可扩展自己的API
 * @Author Ron
 * @CreateTime 2020/9/22 11:59
 */
//运行后会变为knife-goods-server或knife-goods-server01，其中01是指01迭代，多迭代分支时可用到
@FeignClient(name = "${knife.api.goods-server:knife-goods-server}${sprint.branch.version:}"
         ,fallback = GoodsApiFallbackApi.class)
public interface GoodsFeignApi {

    @GetMapping("/goods/{id}")
    Result<GoodsDTO> get(@PathVariable("id") Integer id);

    @GetMapping("/goods/list")
    Result<List<GoodsDTO>> list();

    @GetMapping("/goods/pages")
    Result<IPage<GoodsDTO>> pages(@RequestParam("pageNo") Integer pageNo,
                                  @RequestParam("pageSize") Integer pageSize);
}
