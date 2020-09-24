package com.it235.knife.log.props;

/**
 * @description: 日志输出类型
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 17:31
 */
public enum LogType {

    /**
     * 记录日志到本地
     */
    LOGGER,
    /**
     * 记录日志到数据库
     */
    DB,
    /**
     * 记录日志到KAFKA
     */
    KAFKA,
    ;
}
