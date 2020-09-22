package com.it235.knife.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 22:02
 */
@ComponentScan("com.it235.knife")
@SpringBootApplication
//@EnableFeignClients(basePackages = {"com.it235.knife"})
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}