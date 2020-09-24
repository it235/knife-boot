package com.it235.knife.log.config;

import com.it235.knife.log.aspect.LogAspect;
import com.it235.knife.log.event.LogListener;
import com.it235.knife.log.props.LogProperties;
import com.it235.knife.log.props.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 14:35
 */
@EnableAsync
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = LogProperties.class)
public class LogConfiguration {

    @Autowired
    private LogProperties logProperties;

    @Bean
    public LogListener sysLogListener() {
        return new LogListener(logProperties);
    }

    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }
}
