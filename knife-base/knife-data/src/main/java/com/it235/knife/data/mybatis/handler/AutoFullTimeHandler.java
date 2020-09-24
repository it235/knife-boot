package com.it235.knife.data.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/24 11:29
 */
public class AutoFullTimeHandler implements MetaObjectHandler{

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
