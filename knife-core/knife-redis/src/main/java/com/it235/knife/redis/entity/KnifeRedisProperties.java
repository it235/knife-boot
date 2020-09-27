package com.it235.knife.redis.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 16:15
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "knife.redis")
public class KnifeRedisProperties {
    private int database = 0;
    private String url;
    private String host = "localhost";
    private String password;
    private int port = 6379;
    private boolean ssl;
    private Duration timeout;
    private String clientName;
    private KnifeRedisProperties.Sentinel sentinel;
    private KnifeRedisProperties.Cluster cluster;
    private final KnifeRedisProperties.Jedis jedis = new KnifeRedisProperties.Jedis();
    private final KnifeRedisProperties.Lettuce lettuce = new KnifeRedisProperties.Lettuce();

    public KnifeRedisProperties() {
    }

    public int getDatabase() {
        return this.database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public KnifeRedisProperties.Sentinel getSentinel() {
        return this.sentinel;
    }

    public void setSentinel(KnifeRedisProperties.Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public KnifeRedisProperties.Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(KnifeRedisProperties.Cluster cluster) {
        this.cluster = cluster;
    }

    public KnifeRedisProperties.Jedis getJedis() {
        return this.jedis;
    }

    public KnifeRedisProperties.Lettuce getLettuce() {
        return this.lettuce;
    }

    public static class Lettuce {
        private Duration shutdownTimeout = Duration.ofMillis(100L);
        private KnifeRedisProperties.Pool pool;

        public Lettuce() {
        }

        public Duration getShutdownTimeout() {
            return this.shutdownTimeout;
        }

        public void setShutdownTimeout(Duration shutdownTimeout) {
            this.shutdownTimeout = shutdownTimeout;
        }

        public KnifeRedisProperties.Pool getPool() {
            return this.pool;
        }

        public void setPool(KnifeRedisProperties.Pool pool) {
            this.pool = pool;
        }
    }

    public static class Jedis {
        private KnifeRedisProperties.Pool pool;

        public Jedis() {
        }

        public KnifeRedisProperties.Pool getPool() {
            return this.pool;
        }

        public void setPool(KnifeRedisProperties.Pool pool) {
            this.pool = pool;
        }
    }

    public static class Sentinel {
        private String master;
        private List<String> nodes;

        public Sentinel() {
        }

        public String getMaster() {
            return this.master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

    public static class Cluster {
        private List<String> nodes;
        private Integer maxRedirects;

        public Cluster() {
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirects() {
            return this.maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }

    public static class Pool {
        private int maxIdle = 8;
        private int minIdle = 0;
        private int maxActive = 8;
        private Duration maxWait = Duration.ofMillis(-1L);
        private Duration timeBetweenEvictionRuns;

        public Pool() {
        }

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }

        public Duration getTimeBetweenEvictionRuns() {
            return this.timeBetweenEvictionRuns;
        }

        public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
            this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
        }
    }
    //扩展database
    private List<Integer> databases;
}
