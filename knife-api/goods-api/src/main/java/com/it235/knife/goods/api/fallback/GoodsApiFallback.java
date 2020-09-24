package com.it235.knife.goods.api.fallback;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it235.knife.core.http.Result;
import com.it235.knife.goods.api.GoodsApi;
import com.it235.knife.goods.dto.GoodsDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:59
 */
@Component
public class GoodsApiFallback implements GoodsApi {
    public Result<GoodsDTO> get(int id) {
        return Result.ofSuccess(new GoodsDTO());
    }

    @Override
    public Result<List<GoodsDTO>> list() {
        return null;
    }

    @Override
    public Result<IPage<GoodsDTO>> pages(Integer pageNo, Integer pageSize) {
        return null;
    }
}
