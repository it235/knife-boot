package com.it235.knife.log.event;

import com.it235.knife.log.entity.OperationLog;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 日志监听对象
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 17:01
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(OperationLog source) {
        super(source);
    }
}
