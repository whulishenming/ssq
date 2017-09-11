package com.lsm.ssq.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisKit {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ValueOperations<String, String> opsForValue;
    @Autowired
    private ListOperations<String, String> opsForList;
    @Autowired
    private ZSetOperations<String, String> opsForZSet;

    public Boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public String get(final String key) {
        return opsForValue.get(key);
    }

}
