package com.it235.knife.es.config;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/23 0:14
 */

import com.it235.knife.es.props.ElasticssearchProps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@EnableConfigurationProperties({ElasticssearchProps.class})
public class ElasticsearchConfig {

    @Autowired
    private ElasticssearchProps esProps;

    @Bean
    public RestClient restClient() {
        List<String> hostlist = esProps.getHostlist();
        // 解析hostlist配置信息
        // 创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[hostlist.size()];
        for (int i = 0; i < hostlist.size(); i++) {
            String item = hostlist.get(i);
            String[] split = item.split(":");
            httpHostArray[i] = new HttpHost(split[0], Integer.parseInt(split[1]), "http");
        }
        return RestClient.builder(httpHostArray).build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        String username = esProps.getUsername();
        String password = esProps.getPassword();
        if(Objects.nonNull(username) && Objects.nonNull(password)){
            log.info("ES用户名：{}，密码：{}" ,username,password);
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }
        // 初始化ES客户端的构造器
        RestClientBuilder builder = RestClient.builder(httpHostHandlerDev());
        // 异步的请求配置
        builder.setRequestConfigCallback(builder1 -> {
            // 连接超时时间 默认-1
            Integer connectTimeout = esProps.getConnectTimeout();
            if(connectTimeout != null){
                builder1.setConnectTimeout(connectTimeout);
            }
            Integer socketTimeout = esProps.getSocketTimeout();
            if(socketTimeout != null){
                builder1.setSocketTimeout(socketTimeout);
            }
            Integer connectionRequestTimeout = esProps.getConnectionRequestTimeout();
            if(connectionRequestTimeout != null){
                builder1.setConnectionRequestTimeout(connectionRequestTimeout);
            }
            return builder1;
        });
        // 异步的httpclient连接数配置
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setMaxConnTotal(esProps.getMaxConnTotal());
            httpAsyncClientBuilder.setMaxConnPerRoute(esProps.getMaxConnPerRoute());
            // 赋予连接凭证
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpAsyncClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }

    /**
     * 为了应对集群部署的es，使用以下写法，返回HttpHost数组
     */
    private HttpHost[] httpHostHandlerDev() {
        List<String> hostlist = esProps.getHostlist();
        HttpHost[] httpHosts = new HttpHost[hostlist.size()];
        for (int i = 0; i < hostlist.size(); i++) {
            String[] split = hostlist.get(i).split(":");
            String ip = split[0];
            int port = Integer.parseInt(split[1]);
            httpHosts[i] = new HttpHost(ip, port, "http");
        }
        return httpHosts;
    }
}
