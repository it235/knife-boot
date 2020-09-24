package com.it235.knife.data.mybatis.config;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 16:18
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.it235.knife.data.factory.YamlPropertySourceFactory;
import com.it235.knife.data.mybatis.handler.AutoFullTimeHandler;
import com.it235.knife.data.mybatis.handler.CoustomTenantLineHandler;
import com.it235.knife.data.mybatis.props.TenantProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @package: com.tifang.server.config
 * @description: Mybatis-Plus配置类
 * @author: jianjun.ren
 * @date: Created in 2019/9/24 14:27
 * @copyright: Copyright (c) 2019
 * @version: V1.0
 * @modified: jianjun.ren
 */
@Configuration
@MapperScan("com.it235.knife.*.mapper")
@EnableTransactionManagement
@Slf4j
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:knife-db.yml")
@EnableConfigurationProperties(TenantProperties.class)
public class MybatisPlusConfig {

    /**
     * 需要注意的是该Properties可以由配置中心动态刷新
     */
    @Autowired
    private TenantProperties tenantProperties;

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        //多租户开启
        if(tenantProperties.getEnable()){
            interceptor.addInnerInterceptor(
                    new TenantLineInnerInterceptor(
                            new CoustomTenantLineHandler(tenantProperties)));
        }
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    @ConditionalOnMissingBean(AutoFullTimeHandler.class)
    public AutoFullTimeHandler mateMetaObjectHandler(){
        AutoFullTimeHandler autoFullTimeHandler = new AutoFullTimeHandler();
        log.info("AutoFullTimeHandler [{}]", autoFullTimeHandler);
        return autoFullTimeHandler;
    }
}