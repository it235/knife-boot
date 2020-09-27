package com.it235.knife.redis.config;



import com.alibaba.fastjson.parser.ParserConfig;
import com.it235.knife.common.factory.YamlPropertySourceFactory;
import com.it235.knife.redis.entity.KnifeRedisProperties;
import com.it235.knife.redis.manager.RedisManager;
import com.it235.knife.redis.serializer.CustomFastJsonRedisSerializer;
import com.it235.knife.redis.serializer.CustomStringRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: Redis配置类
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 11:15
 */
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:knife-redis.yml")
@EnableConfigurationProperties({KnifeRedisProperties.class})
@AutoConfigureBefore({RedisAutoConfiguration.class})
@Import(KnifeRedisRegister.class)
@EnableCaching
@Configuration
@Slf4j
public class FastJsonRedisConfig implements ApplicationContextAware {
    public static final Map<String,RedisTemplate> map = new HashMap<>();
    @Autowired
    private KnifeRedisProperties knifeRedisProperties;

    public Map<String,RedisTemplate> initRedisTemp(){
        Map<String,RedisTemplate> map = new HashMap<>();
        List<Integer> databases = knifeRedisProperties.getDatabases();
        for (Integer database : databases) {
            String key = "redisTemplate" + database;
            RedisTemplate redisTemplate = applicationContext.getBean(key , RedisTemplate.class);
            if(redisTemplate != null){
                map.put(key , redisTemplate);
            }
        }
        System.out.println("map size -> " + map.size());
        return map;
    }
//
//    private int defaultDb;
//    private Integer[] dbs = {0,1,2};
//
//    public static Map<Integer, RedisTemplate<Serializable, Object>> redisTemplateMap = new HashMap<>();
//
//    @Autowired
//    private RedisConnectionFactory factory;
//    @PostConstruct
//    public void initRedisTemp() throws Exception {
//        log.info("###### START 初始化 Redis 连接池 START ######");
//        defaultDb = dbs[0];
//        for (Integer db : dbs) {
//            log.info("###### 正在加载Redis-db-" + db+ " ######");
//            redisTemplateMap.put(db, redisTemplateObject(db));
//        }
//        log.info("###### END 初始化 Redis 连接池 END ######");
//    }
//
//    public RedisTemplate<Serializable, Object> redisTemplateObject(Integer dbIndex) {
//        RedisTemplate<Serializable, Object> redisTemplateObject = new RedisTemplate<>();
//        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory)factory;
//        lettuceConnectionFactory.setShareNativeConnection(false);
//        lettuceConnectionFactory.setDatabase(dbIndex);
//        redisTemplateObject.setConnectionFactory(lettuceConnectionFactory);
//        setSerializer(redisTemplateObject);
//        redisTemplateObject.afterPropertiesSet();
//        return redisTemplateObject;
//    }
//    public RedisTemplate<Serializable, Object> getRedisTemplateByDb(int db){
//        return redisTemplateMap.get(db);
//    }
//
//    public RedisTemplate<Serializable, Object> getRedisTemplate(){
//        return redisTemplateMap.get(defaultDb);
//    }

    @Bean
    @ConditionalOnClass(RedisOperations.class)
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> template = new RedisTemplate <>();
        template.setConnectionFactory(factory);

//        ExtendsJackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new ExtendsJackson2JsonRedisSerializer<>();
        CustomFastJsonRedisSerializer<Object> jackson2JsonRedisSerializer = new CustomFastJsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(false);
        CustomStringRedisSerializer stringRedisSerializer = new CustomStringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Bean
    public RedisManager redisManager(){
        return new RedisManager(initRedisTemp());
    }


//    private void setSerializer(RedisTemplate<Serializable, Object> template) {
//        CustomFastJsonRedisSerializer<Object> jackson2JsonRedisSerializer = new CustomFastJsonRedisSerializer<>(Object.class);
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(false);
//        CustomStringRedisSerializer stringRedisSerializer = new CustomStringRedisSerializer();
//        // key采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        // hash的key也采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        // value序列化方式采用jackson
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        // hash的value序列化方式采用jackson
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//    }

/*
    *//**
     * 创建restTemplate相同配置,但dbIndex不同的RestTemplate, 可以理解为选库
     *
     * @param redisTemplate
     * @param dbIndex redis库
     * @return
     *//*
    public static RedisTemplate selectDb(RedisTemplate redisTemplate, int dbIndex){
        try {
            RedisTemplate dbSelectRedisTemplate = redisTemplate.getClass().getConstructor().newInstance();
            BeanUtils.copyProperties(redisTemplate, dbSelectRedisTemplate);
            RedisConnectionFactory connectionFactory = dbSelectRedisTemplate.getConnectionFactory();
            RedisConnectionFactory dbSelectConnectionFactory = createDbSelectConnectionFactory(connectionFactory, dbIndex);
            dbSelectRedisTemplate.setConnectionFactory(dbSelectConnectionFactory);
            dbSelectRedisTemplate.afterPropertiesSet();
            return dbSelectRedisTemplate;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


    }


    protected static RedisConnectionFactory createDbSelectConnectionFactory(RedisConnectionFactory connectionFactory, int dbIndex){
        RedisConnectionFactory dbSelectConnectionFactory = null;
        if(connectionFactory instanceof LettuceConnectionFactory){
            dbSelectConnectionFactory= createLettuceDbSelectFactory((LettuceConnectionFactory)connectionFactory , dbIndex);
        }else {
            // 由于通过创建一个连接工厂比较复杂(BeanUtils复制属性有限制, 需要了解连接工厂内部构造), 暂不创建其他连接工厂
            throw new RuntimeException("不能识别类型: "+connectionFactory.getClass());
        }
        return dbSelectConnectionFactory;
    }




    // --------------------------------------
    // lettuceConnectionFactory, 创建后的connection在共享连接下不支持选择库 (connection#select),
    // 调用#setShareNativeConnection(false)后可以选库

    // !!! 注意事项: 使用BeanUtils复制属性, 属性必须添加set，get方法，否则拷贝不成功，但是不报错
    // 由于创建一个相同配置但dbIndex不同的方法比较复杂, 使用前需要仔细测试
    private static LettuceConnectionFactory createLettuceDbSelectFactory(LettuceConnectionFactory connectionFactory, int dbIndex){
        LettuceConnectionFactory dbSelectConnectionFactory = new LettuceDbSelectConnectionFactory(dbIndex);
        BeanUtils.copyProperties(connectionFactory, dbSelectConnectionFactory);
        //构造参数传入的属性(因为没有setter, BeanUtils不能复制的属性)
        final String[] constructProperties=new String[]{"clientConfiguration", "configuration"};
        MyBeanUtils.forceCopyProperties(connectionFactory, dbSelectConnectionFactory, constructProperties);
        dbSelectConnectionFactory.afterPropertiesSet();
        final String[] equalProperties=new String[]{"clientConfiguration", "configuration"};
        final String[] notEqualProperties=new String[]{"client","pool", "connectionProvider","reactiveConnectionProvider"};
        final String[] sameTypeProperties=new String[]{"connectionProvider","reactiveConnectionProvider"};
        MyBeanUtils.assertPropertiesEquals(connectionFactory, dbSelectConnectionFactory, equalProperties);
        MyBeanUtils.assertPropertiesNotEquals(connectionFactory, dbSelectConnectionFactory, notEqualProperties);
        MyBeanUtils.assertSameTypes(connectionFactory, dbSelectConnectionFactory, sameTypeProperties);
        return dbSelectConnectionFactory;
    }

    @Slf4j
    private static class LettuceDbSelectConnectionFactory extends LettuceConnectionFactory{

        private int pointDbIndex;

        public LettuceDbSelectConnectionFactory(int pointDbIndex) {
            this.pointDbIndex = pointDbIndex;
        }

        *//**
         * 替换原配置的dbIndex
         * @return
         *//*
        @Override
        public int getDatabase() {
            log.debug("使用redis库{}",pointDbIndex);
            return pointDbIndex;
        }
    }

    *//**
     * key, value 都是字符串
     * 使用3号库
     * @param stringRedisTemplate 由{@link }实例化stringRedisTemplate
     * @return
     *//*
    @Bean("string3RedisManager")
    public RedisManager<String,String> string3RedisManager(
            @Qualifier("stringRedisTemplate")
                    RedisTemplate<String,String> stringRedisTemplate){

        RedisTemplate<String,String> string3RedisTemplate = selectDb(stringRedisTemplate, 3);
        return new RedisManager<>(string3RedisTemplate);
    }
    @Bean("string4RedisManager")
    public RedisManager<String,String> string4RedisManager(
            @Qualifier("stringRedisTemplate")
                    RedisTemplate<String,String> stringRedisTemplate){

        RedisTemplate<String,String> string4RedisTemplate = selectDb(stringRedisTemplate, 4);
        return new RedisManager<>(string4RedisTemplate);
    }*/

}
