package com.cache.demo.config.myCache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 自定义缓存管理器
 *
 * @author yangjianzhong
 * @create 2021-11-08 10:12 上午
 **/
public class MyCacheManager implements CacheManager {

    private ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private Set<String> cacheNames;
    private RedisTemplate<String, Object> redisTemplate;
    private MyCacheConfig myCacheConfig;

    public MyCacheManager(RedisTemplate<String, Object> redisTemplate, MyCacheConfig myCacheConfig) {
        this.cacheNames = myCacheConfig.getCacheNames();
        this.redisTemplate = redisTemplate;
        this.myCacheConfig = myCacheConfig;
    }

    @Override
    public Cache getCache(String name) {
        if (name == null || name == "") {
            throw new IllegalArgumentException("缓存的cache名称不能为空");
        }
        Cache cache = cacheMap.get(name);
        if (cache == null) {

            cache = new MyCache(name, redisTemplate, getCaffeineCache(), myCacheConfig);
            cacheMap.put(name, cache);
        }
        return cache;
    }

    private com.github.benmanes.caffeine.cache.Cache<String, Object> getCaffeineCache() {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (myCacheConfig.getCaffeine().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(myCacheConfig.getCaffeine().getExpireAfterWrite(), TimeUnit.SECONDS);
        }
        if (myCacheConfig.getCaffeine().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(myCacheConfig.getCaffeine().getRefreshAfterWrite(), TimeUnit.SECONDS);
        }
        if (myCacheConfig.getCaffeine().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(myCacheConfig.getCaffeine().getInitialCapacity());
        }
        if (myCacheConfig.getCaffeine().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(myCacheConfig.getCaffeine().getMaximumSize());
        }
        return cacheBuilder.build();
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheNames;
    }
}
