package com.it235.knife.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 0:06
 */
@SpringBootApplication(scanBasePackages = "com.it235.knife")
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

}
