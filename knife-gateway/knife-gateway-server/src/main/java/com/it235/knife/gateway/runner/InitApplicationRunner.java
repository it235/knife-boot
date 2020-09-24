package com.it235.knife.gateway.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;

/**
 * Boot服务启动后的初始化类
 *
 * @author it235
 * @createTime 2020-09-18 23:02
 */
@Slf4j
@Component
public class InitApplicationRunner implements ApplicationRunner {

    @Value("${spring.application.name:}")
    private String appName;

    @Value("${server.port:}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.mvc.servlet.path:}")
    private String servletPath;

    public void run(ApplicationArguments args) throws Exception {
        String host = InetAddress.getLocalHost().getHostAddress();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}{}{}/doc.html\n" +
                "----------------------------------------------------------",
                appName,host,port,contextPath,servletPath);
    }
}
