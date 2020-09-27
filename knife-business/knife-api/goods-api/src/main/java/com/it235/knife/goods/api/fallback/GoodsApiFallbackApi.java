package com.it235.knife.goods.api.fallback;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.it235.knife.common.http.Result;
import com.it235.knife.goods.api.GoodsFeignApi;
import com.it235.knife.goods.dto.GoodsDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:59
 */
@Component
@RequestMapping("/fallback")
public class GoodsApiFallbackApi implements GoodsFeignApi {

    public Result<GoodsDTO> get(Integer id) {
        return Result.ofSuccess(new GoodsDTO());
    }

    public Result<List<GoodsDTO>> list() {
        return null;
    }

    public Result<IPage<GoodsDTO>> pages(Integer pageNo, Integer pageSize) {
        return null;
    }
}
