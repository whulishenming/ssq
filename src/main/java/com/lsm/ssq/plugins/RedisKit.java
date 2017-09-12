package com.lsm.ssq.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

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
    @Autowired
    private HashOperations<String, String, String> opsForHash;

    public Boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public String get(final String key) {
        return opsForValue.get(key);
    }

    public void set(final String key, final String value) {
        opsForValue.set(key, value);
    }

    public void hSet(String key, String field, String value){
        opsForHash.put(key, field, value);
    }

    public String hGet(final String key, final String field) {
        return opsForHash.get(key, field);
    }

    public Boolean hExists(final String key, String field){
        return opsForHash.hasKey(key, field);
    }

    public Boolean zAdd(final String key, final String member, final double score) {
        return opsForZSet.add(key, member, score);
    }

    public Double zIncrby(final String key, final String member, final double increment){
        return opsForZSet.incrementScore(key, member, increment);
    }

    public Set<String> zRange(final String key, final long start, final long end){
        return opsForZSet.range(key, start, end);
    }

    public Set<String> zRevRange(final String key, final long start, final long end){
        return opsForZSet.reverseRange(key, start, end);
    }

}
