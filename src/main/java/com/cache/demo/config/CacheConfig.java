package com.cache.demo.config;

import com.cache.demo.config.myCache.MyCacheConfig;
import com.cache.demo.config.myCache.MyCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * @author yangjianzhong
 * @create 2021-03-24 下午5:03
 **/
@Configuration
public class CacheConfig {

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    public MyCacheConfig myCacheConfig;

    @Bean
    public CacheManager cacheManager() {
        return new MyCacheManager(redisTemplate, myCacheConfig);
    }

}
