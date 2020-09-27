package com.it235.knife.redis.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.it235.knife.redis.entity.LettuceEnity;
import com.it235.knife.redis.entity.PoolEntity;
import com.it235.knife.redis.entity.RedisEntity;
import com.it235.knife.redis.serializer.CustomFastJsonRedisSerializer;
import com.it235.knife.redis.serializer.CustomStringRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 15:27
 */
public class KnifeRedisRegister implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(KnifeRedisRegister.class);

    private static Map<String, Object> registerBean = new ConcurrentHashMap<>();

    private Environment environment;
    private Binder binder;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.binder = Binder.get(this.environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        RedisEntity redisEntity;
        try {
            redisEntity = binder.bind("knife.redis", RedisEntity.class).get();
        } catch (NoSuchElementException e) {
            logger.error("Failed to configure knife redis: 'knife.redis' attribute is not specified and no embedded redis could be configured.");
            return;
        }
        boolean onPrimary = true;

        //根据多个库实例化出多个连接池和Template
        List<Integer> databases = redisEntity.getDatabases();
        for (Integer database : databases) {
            //单机模式
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setHostName(String.valueOf(redisEntity.getHost()));
            configuration.setPort(Integer.parseInt(String.valueOf(redisEntity.getPort())));
            configuration.setDatabase(database);
            String password = redisEntity.getPassword();
            if (password != null && !"".equals(password)) {
                RedisPassword redisPassword = RedisPassword.of(password);
                configuration.setPassword(redisPassword);
            }

            //池配置
            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();

            RedisProperties.Pool pool = redisEntity.getLettuce().getPool();
            genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
            genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
            genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
            if (pool.getMaxWait() != null) {
                genericObjectPoolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
            }
            Supplier<LettuceConnectionFactory> lettuceConnectionFactorySupplier = () -> {
                LettuceConnectionFactory factory = (LettuceConnectionFactory) registerBean.get("LettuceConnectionFactory" + database);
                if (factory != null) {
                    return factory;
                }
                LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
                Duration shutdownTimeout = redisEntity.getLettuce().getShutdownTimeout();
                if(shutdownTimeout == null){
                    shutdownTimeout = binder.bind("knife.redis.shutdown-timeout", Duration.class).get();
                }
                if (shutdownTimeout != null) {
                    builder.shutdownTimeout(shutdownTimeout);
                }
                LettuceClientConfiguration clientConfiguration = builder.poolConfig(genericObjectPoolConfig).build();
                factory = new LettuceConnectionFactory(configuration, clientConfiguration);
                registerBean.put("LettuceConnectionFactory" + database, factory);
                return factory;
            };

            LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionFactorySupplier.get();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(LettuceConnectionFactory.class, lettuceConnectionFactorySupplier);
            AbstractBeanDefinition factoryBean = builder.getRawBeanDefinition();
            factoryBean.setPrimary(onPrimary);
            beanDefinitionRegistry.registerBeanDefinition("lettuceConnectionFactory" + database, factoryBean);
            // StringRedisTemplate
            GenericBeanDefinition stringRedisTemplate = new GenericBeanDefinition();
            stringRedisTemplate.setBeanClass(StringRedisTemplate.class);
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, lettuceConnectionFactory);
            stringRedisTemplate.setConstructorArgumentValues(constructorArgumentValues);
            stringRedisTemplate.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
            beanDefinitionRegistry.registerBeanDefinition("stringRedisTemplate" + database, stringRedisTemplate);
            // RedisTemplate
            GenericBeanDefinition redisTemplate = new GenericBeanDefinition();
            redisTemplate.setBeanClass(RedisTemplate.class);
            redisTemplate.getPropertyValues().add("connectionFactory", lettuceConnectionFactory);
            redisTemplate.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);


//        ExtendsJackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new ExtendsJackson2JsonRedisSerializer<>();
            CustomFastJsonRedisSerializer<Object> jackson2JsonRedisSerializer = new CustomFastJsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
            ParserConfig.getGlobalInstance().setAutoTypeSupport(false);
            CustomStringRedisSerializer stringRedisSerializer = new CustomStringRedisSerializer();
            // key采用String的序列化方式
            redisTemplate.getPropertyValues().add("keySerializer", stringRedisSerializer);
            // hash的key也采用String的序列化方式
            redisTemplate.getPropertyValues().add("hashKeySerializer", stringRedisSerializer);
            // value序列化方式采用jackson
            redisTemplate.getPropertyValues().add("valueSerializer", jackson2JsonRedisSerializer);
            // hash的value序列化方式采用jackson
            redisTemplate.getPropertyValues().add("hashValueSerializer", jackson2JsonRedisSerializer);

            beanDefinitionRegistry.registerBeanDefinition("redisTemplate" + database, redisTemplate);
            logger.info("Registration redis ({}) !", database);
            if (onPrimary) {
                onPrimary = false;
            }
        }
    }
}
