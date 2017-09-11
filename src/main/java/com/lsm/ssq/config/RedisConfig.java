package com.lsm.ssq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private Integer maxWait;

    @Value("${spring.redis.database}")
    private Integer database;


    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory getConnectionFactory(){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        factory.setPoolConfig(config);
        factory.setHostName(host);
        factory.setPort(port);
        // 选择数据库
        factory.setUsePool(true);
        factory.setDatabase(database);
        log.info("JedisConnectionFactory bean init success.");
        return factory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> getRedisTemplate(JedisConnectionFactory jedisConnectionFactory){

        return new StringRedisTemplate(jedisConnectionFactory);
    }

    /**
     * 对字符串(String)的操作
     */
    @Bean(name = "opsForValue")
    public ValueOperations<String, String> opsForValue(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对哈希(Hash)的操作
     */
    @Bean(name = "opsForHash")
    public HashOperations<String, String, String> opsForHash(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对列表(list)的操作
     */
    @Bean(name = "opsForList")
    public ListOperations<String, String> opsForList(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对集合(Set)的操作
     */
    @Bean(name = "opsForSet")
    public SetOperations<String, String> opsForSet(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合(zset)的操作
     */
    @Bean(name = "opsForZSet")
    public ZSetOperations<String, String> opsForZSet(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {

        return redisTemplate.opsForZSet();
    }


}
