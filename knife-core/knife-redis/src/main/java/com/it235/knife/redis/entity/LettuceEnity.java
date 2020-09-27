package com.it235.knife.redis.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 16:21
 */
@Getter
@Setter
public class LettuceEnity extends RedisProperties.Lettuce {
    private PoolEntity poolEntity;
}
