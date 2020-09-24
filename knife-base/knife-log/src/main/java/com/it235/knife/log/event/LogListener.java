package com.it235.knife.log.event;

import cn.hutool.core.util.StrUtil;
import com.it235.knife.common.tl.TenantTreadLocal;
import com.it235.knife.log.entity.OperationLog;
import com.it235.knife.log.props.LogProperties;
import com.it235.knife.log.props.LogType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @description: 异步监听事件日志事件
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 17:02
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class LogListener {

    private LogProperties logProperties;

    /**
     * 异步方法
     * @param event
     */
    @Async
    @Order
    @EventListener({LogEvent.class})
    public void saveSysLog(LogEvent event) {
        //拿到事件源
        OperationLog operation = (OperationLog)event.getSource();
        //租户设置
        if (operation != null && !StrUtil.isEmpty(operation.getTenantId())
                && StrUtil.isEmpty(TenantTreadLocal.getTenantId())) {
            TenantTreadLocal.setTenantId(operation.getTenantId());
        }
        //由具体的服务提供实现来进行存储
        LogType logType = logProperties.getLogType();
        if(logType == LogType.KAFKA){
            //走KAFKA消息队列存储
            System.out.println("DB");
        } else {
            //调用DB或本地存储远程方法，或投递DB本地存储消息
            System.out.println("本地存储");
        }
        log.info(operation.toString());
    }
}
