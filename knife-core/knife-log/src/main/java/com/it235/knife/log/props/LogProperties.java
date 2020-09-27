package com.it235.knife.log.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 17:13
 */
@Getter
@Setter
@ConfigurationProperties(LogProperties.PREFIX)
public class LogProperties {
    /**
     * 前缀
     */
    public static final String PREFIX = "knife.log";

    /**
     * 是否启用
     */
    private Boolean enable = false;

    /**
     * 记录日志类型：
     * local
     * db
     * kafa
     */
    private LogType logType = LogType.DB;

}
