package com.it235.knife.redis.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 16:03
 */
@Getter
@Setter
public class RedisEntity {
    private String host;
    private Integer port;
    private String password;
    private List<Integer> databases;
    private Duration timeout;
    private LettuceEnity lettuce;
}
