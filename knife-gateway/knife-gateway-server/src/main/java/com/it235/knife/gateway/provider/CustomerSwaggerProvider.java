package com.it235.knife.gateway.provider;

import com.alibaba.fastjson.JSONArray;
import com.it235.knife.gateway.constants.GatewayConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 聚合各个服务的swagger接口
 * 主要思路是：
 *          1.从网关配置路由列表中取出所有断言及Path
 *          2.与已经加载的网关路由定位表进行对比，过滤掉无需swagger的路由信息
 *          3.从断言中匹配出有Path值的路由，并循环对其发送Swagger资源获取请求
 *          4.将返回结果封装成SwaggerResource，聚合成一个List返回出来
 * @Author Ron
 * @CreateTime 2020/9/21 23:07
 */
@Component
@Primary
@Slf4j
@AllArgsConstructor
public class CustomerSwaggerProvider implements SwaggerResourcesProvider, ApplicationListener<WebServerInitializedEvent> {
    /**
     * 获取后端服务swagger2资源的url
     * 获取结果：[{"name":"groupName","url":"/v2/api-docs","swaggerVersion":"2.0","location":"/v2/api-docs"}]
     */
    private static final String DEFAULT_SWAGGER_RESOURCES_URI = "/swagger-resources";

    /**
     * 网关路由定位器，用来获取路由信息
     */
    private final RouteLocator routeLocator;

    @Autowired
    @Qualifier("lbRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    private GatewayProperties gatewayProperties;

    @Autowired
    public CustomerSwaggerProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        //最终返回的swagger资源
        List<SwaggerResource> resources = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe((route -> routes.add(route.getId())));

        //遍历网关配置中的路由集合
        gatewayProperties.getRoutes().stream()
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> {
                    //再遍历子项断言集合
                    routeDefinition.getPredicates().stream()
                            //匹配断言中有Path属性的路由（predicates），只有这种路由才需要生成swagger文档
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            .forEach(predicateDefinition -> {
                                URI uri = routeDefinition.getUri();

                                String definitionId = routeDefinition.getId();
                                try {
                                    String serverURI = currentServerURL + uri.getHost() + DEFAULT_SWAGGER_RESOURCES_URI;
                                    //依次获取后端服务的swagger资源
                                    JSONArray list = restTemplate.getForObject(serverURI , JSONArray.class);
                                    if (!list.isEmpty()) {
                                        for (int i = 0; i < list.size(); i++) {
                                            SwaggerResource sr = list.getObject(i, SwaggerResource.class);
                                            //对获取到的Swagger资源进行封装
                                            resources.add(swaggerResource(
                                                    sr.getName() + ":" + definitionId , "/" + definitionId + sr.getUrl()));
                                        }
                                    }
                                } catch (Exception e) {
                                    log.warn("加载后端资源时失败{}，请确认后端服务是否已经启动，错误信息：{}",
                                            uri.getHost() , e.getMessage());
                                    //swagger下拉列表资源候补，进入此处的添加仅仅只会出现在右侧资源列表中，待后端服务启动后才能正确查看文档
                                    resources.add(swaggerResource(uri.getHost() + ":" + definitionId,
                                            predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                                    .replace("/**", "/v2/api-docs?group=" + uri.getHost())));
                                }
                            });
                });

        return resources;
    }

    /**
     * 封装swagger对象属性至 SwaggerResource
     * @param name
     * @param location
     * @return
     */
    private SwaggerResource swaggerResource(String name, String location) {
        log.info("swaggerResource -> name:{},location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(GatewayConstant.KNIFE_VERSION);
        return swaggerResource;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int serverPort = event.getWebServer().getPort();
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //host：http://gateway_ip:port
        currentServerURL = "http://" + address.getHostAddress() + ":" + serverPort;
    }
    //当前网关的访问
    private String currentServerURL;

}
