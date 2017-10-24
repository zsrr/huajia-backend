package com.sssta.huajia.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisService {
    JedisPool jedisPool;

    public JedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    protected Jedis getJedis() {
        return jedisPool.getResource();
    }
}
