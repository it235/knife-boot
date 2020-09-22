package com.it235.knife.goods.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:44
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @GetMapping("{id}")
    public String get(@PathVariable("id") String goodsId){
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "深入理解JVM虚拟机" + goodsId;
    }
}
