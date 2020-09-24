package com.it235.knife.order.controller;

import com.it235.knife.core.http.Result;
import com.it235.knife.goods.dto.GoodsDTO;
import com.it235.knife.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制层
 *
 * @Author Ron
 * @CreateTime 2020/9/22 11:27
 */
@Api(value = "订单（order）相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "根据订单编号查询订单信息")
    @GetMapping("{orderNumber}")
    public Result<GoodsDTO> get(@PathVariable String orderNumber){
        GoodsDTO goodsDTO = orderService.get("20200100101");
        return Result.ofSuccess(goodsDTO);
    }
}
