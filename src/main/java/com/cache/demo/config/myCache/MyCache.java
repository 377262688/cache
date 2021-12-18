package com.cache.demo.config.myCache;

import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangjianzhong
 * @create 2021-11-08 10:21 上午
 **/
public class MyCache extends AbstractValueAdaptingCache {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String name;

    private final RedisTemplate redisTemplate;

    private final String keyPrefix;

    private CacheMode cacheMode;

    private Cache<String, Object> caffeineCache;

    private long defaultExpiration = 0;

    private Map<String, Long> expires;


    protected MyCache(final String name, RedisTemplate redisTemplate, Cache<String, Object> cache,MyCacheConfig myCacheConfig) {
        super(false);
        this.name = name;
        this.keyPrefix = myCacheConfig.getKeyPrefix();
        this.cacheMode = myCacheConfig.getMode();
        this.defaultExpiration = myCacheConfig.getRedis().getDefaultExpiration();
        this.expires = myCacheConfig.getRedis().getExpires();

        this.caffeineCache = cache;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Object lookup(Object key) {
        final String cacheKey = this.getKey(key);
        Object value = null;

        if (!this.cacheMode.equals(CacheMode.ONLY_REDIS)) {
            value = this.caffeineCache.getIfPresent(cacheKey);
            if (value != null) {
                log.debug("从内存缓存中获取缓存值, 键值是: {}", cacheKey);
                return value;
            }
        }

        if (!this.cacheMode.equals(CacheMode.ONLY_CAFFEINE)) {
            value = this.readFromRedis(cacheKey);
            if (value != null) {
                log.debug("从Redis缓存中获取缓存值, 键值是: {}", cacheKey);
                if (!this.cacheMode.equals(CacheMode.ONLY_REDIS)) {
                    this.caffeineCache.put(cacheKey, value);
                }
            }
        }
        return value;
    }

    private Object readFromRedis(String cacheKey) {
        return redisTemplate.opsForValue().get(cacheKey);
    }

    /**
     * Return the cache name.
     */
    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public Object getNativeCache() {
        return this;
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = this.lookup(key);
        if (value != null) {
            return (T)value;
        }

        final ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            value = this.lookup(key);
            if (value != null) {
                return (T)value;
            }
            value = valueLoader.call();
            this.refresh(key, this.toStoreValue(value));
            return (T)value;
        } catch (final Exception ex) {
            log.error(MessageFormat.format("获取缓存异常 键值为：{}", key), ex);
            return null;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void put(Object key, Object value) {
        this.refresh(key, value);
    }


    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        final String cacheKey = this.getKey(key);
        final Object prevValue = this.readFromRedis(cacheKey);
        if (prevValue == null) {
            this.refresh(key, value);
        }
        return this.toValueWrapper(prevValue);
    }


    @Override
    public void evict(Object key) {
        final String cacheKey = this.getKey(key);

        if (!this.cacheMode.equals(CacheMode.ONLY_CAFFEINE)) {
            this.deleteToRedis(cacheKey);
        }
        if (!this.cacheMode.equals(CacheMode.ONLY_REDIS)) {
            this.caffeineCache.invalidate(cacheKey);
        }
    }

    private void deleteToRedis(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }

    @Override
    public void clear() {

    }

    private void refresh(@NonNull final Object key, @NonNull final Object value) {
        final String cacheKey = this.getKey(key);
        if (!this.cacheMode.equals(CacheMode.ONLY_CAFFEINE)) {
            final long expire = this.getExpire();
            if (expire > 0) {
                try {
                    this.redisTemplate.opsForValue().set(cacheKey, this.toStoreValue(value), expire, TimeUnit.SECONDS);
                } catch (final Exception ex) {
                    log.error("缓存至REDIS异常", ex);
                }
            }
        }
        if (!this.cacheMode.equals(CacheMode.ONLY_REDIS)) {
            this.caffeineCache.put(cacheKey, this.toStoreValue(value));
        }
    }

    private String getKey(@NonNull final Object key) {
        return this.name.concat(":").concat(
                StringUtils.isEmpty(this.keyPrefix) ? key.toString() : this.keyPrefix.concat(":").concat(key.toString()));
    }

    private long getExpire() {
        final Long cacheNameExpire = this.expires.get(this.name);
        return cacheNameExpire == null ? this.defaultExpiration : cacheNameExpire;
    }
}
