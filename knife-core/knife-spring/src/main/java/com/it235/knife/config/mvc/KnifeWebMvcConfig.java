package com.it235.knife.config.mvc;

import com.it235.knife.filter.WebTraceFilter;
import com.it235.knife.spring.SpringContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: mvc相关配置
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 17:50
 */
@Configuration
public class KnifeWebMvcConfig implements WebMvcConfigurer {

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
    @Bean
    public SpringContextHolder springContextHolder(ApplicationContext applicationContext){
        return new SpringContextHolder(applicationContext);
    }

    @Bean
    public WebTraceFilter traceFilter() {
        return new WebTraceFilter();
    }
}