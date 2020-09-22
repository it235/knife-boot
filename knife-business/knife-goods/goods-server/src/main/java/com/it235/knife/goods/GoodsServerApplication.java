package com.it235.knife.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:42
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.it235.knife"})
@ComponentScan("com.it235.knife")
public class GoodsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsServerApplication.class, args);
    }
}
