package com.it235.knife.redis.serializer;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 12:05
 */

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.Charset;


/**
 * 必须重写序列化器，否则@Cacheable注解的key会报类型转换错误
 */
public class CustomStringRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    private final String target = "\"";

    private final String replacement = "";

    public CustomStringRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public CustomStringRedisSerializer(Charset charset) {
//        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(Object object) {
        String string = JSON.toJSONString(object);
        if (string == null) {
            return null;
        }
        string = string.replace(target, replacement);
        return string.getBytes(charset);
    }
}
