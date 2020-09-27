package com.it235.knife.redis.serializer;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 12:05
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CustomFastJsonRedisSerializer<T> extends Jackson2JsonRedisSerializer<T> {

    private final String target = "\"";

    private final String replacement = "";

    private Class<T> clazz;

    public CustomFastJsonRedisSerializer(Class<T> type) {
        super(type);
        this.clazz = type;
    }


    @Override
    public byte[] serialize(Object object) {
        if(object instanceof String){
            String string = JSON.toJSONString(object);
            string = string.replace(target, replacement);
            return string.getBytes(DEFAULT_CHARSET);
        }else{
            return super.serialize(object);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        try {
            //取出结果转换成Object
            Object typeObject = JSON.parse(str);
            if (typeObject instanceof JSONArray) {
                //如果是集合类型，则转换成JSONArray
                List<T> ts = new ArrayList<>();
                JSONArray objects = JSONArray.parseArray(str);
                for (Object object : objects) {
                    ts.add(JSONObject.parseObject(JSON.toJSONString(object) ,clazz));
                }
                return (T)ts;
            } else if (typeObject instanceof JSONObject) {
                //如果是对象类型则转换成JSONObject
                T t = JSONObject.parseObject(str, clazz);
                return t;
            } else {
                //否则转换成String
                return (T)str;
            }
        } catch (Exception ex) {
            try{
                return (T) str;
            }catch (Exception e) {
                throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
            }
        }
    }
}