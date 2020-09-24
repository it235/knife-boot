package com.it235.knife.es.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 10:27
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "elasticsearch.config")
public class ElasticssearchProps {

    private List<String> hostlist = new ArrayList<>();
    private String username;
    private String password;
    /**
     * 连接超时时间 默认-1
     */
    private Integer connectTimeout = -1;
    private Integer socketTimeout = -1;
    /**
     * 获取连接的超时时间
     */
    private Integer connectionRequestTimeout = -1;
    /**
     * 最大连接数
     */
    private Integer maxConnTotal = 0;
    /**
     * 最大路由连接数
     */
    private Integer maxConnPerRoute = 0;
}
