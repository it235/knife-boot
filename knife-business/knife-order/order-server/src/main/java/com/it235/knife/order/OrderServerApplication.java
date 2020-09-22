package com.it235.knife.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Ron
 * @CreateTime 2020/9/22 11:19
 */
@ComponentScan("com.it235.knife")
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.it235.knife"})
public class OrderServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServerApplication.class, args);
    }
}
