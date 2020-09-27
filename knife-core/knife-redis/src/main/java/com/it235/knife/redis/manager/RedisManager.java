package com.it235.knife.redis.manager;

import com.it235.knife.redis.config.FastJsonRedisConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 14:32
 */

public class RedisManager<String,Object> {

    private Map<String, RedisTemplate> redisTemplateMap;

    public RedisManager(Map<String, RedisTemplate> redisTemplateMap) {
        this.redisTemplateMap = redisTemplateMap;
    }

    public RedisTemplate redisTemplate(int dbIndex) {
        RedisTemplate redisTemplate = redisTemplateMap.get("redisTemplate" + dbIndex);
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

//    public StringRedisTemplate stringRedisTemplate(String name) {
//        RedisTemplate redisTemplate = FastJsonRedisConfig.map.get("stringRedisTemplate" + dbIndex);
//        StringRedisTemplate stringRedisTemplate;
//
//        stringRedisTemplate.setEnableTransactionSupport(true);
//        return stringRedisTemplate;
//    }
}
