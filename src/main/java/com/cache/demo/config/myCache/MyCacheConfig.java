package com.cache.demo.config.myCache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 缓存配置
 *
 * @author yangjianzhong
 * @create 2021-11-08 10:14 上午
 **/
@ConfigurationProperties(prefix = "spring.key.cache.prefix")
public class MyCacheConfig {

    private String keyPrefix;

    /**
     * 缓存名称
     */
    private Set<String> cacheNames = new HashSet<>();

    /**
     * 缓存模式
     */
    private CacheMode mode;

    private Redis redis = new Redis();

    private Caffeine caffeine = new Caffeine();

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Set<String> getCacheNames() {
        return cacheNames;
    }

    public void setCacheNames(Set<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    public CacheMode getMode() {
        return mode;
    }

    public void setMode(CacheMode mode) {
        this.mode = mode;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public Caffeine getCaffeine() {
        return caffeine;
    }

    public void setCaffeine(Caffeine caffeine) {
        this.caffeine = caffeine;
    }

    public class Caffeine {

        /**
         * 写入后过期时间，单位秒
         */
        private long expireAfterWrite;

        /**
         * 写入后刷新时间，单位秒
         */
        private long refreshAfterWrite;

        /**
         * 初始化大小
         */
        private int initialCapacity;

        /**
         * 最大缓存对象个数，超过此数量时之前放入的缓存将失效
         */
        private long maximumSize;

        public long getExpireAfterWrite() {
            return expireAfterWrite;
        }

        public void setExpireAfterWrite(long expireAfterWrite) {
            this.expireAfterWrite = expireAfterWrite;
        }

        public long getRefreshAfterWrite() {
            return refreshAfterWrite;
        }

        public void setRefreshAfterWrite(long refreshAfterWrite) {
            this.refreshAfterWrite = refreshAfterWrite;
        }

        public int getInitialCapacity() {
            return initialCapacity;
        }

        public void setInitialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
        }

        public long getMaximumSize() {
            return maximumSize;
        }

        public void setMaximumSize(long maximumSize) {
            this.maximumSize = maximumSize;
        }
    }

    public class Redis {

        /**
         * 全局过期时间，单位秒，默认不过期
         */
        private long defaultExpiration = 60;

        /**
         * 每个cacheName的过期时间，单位秒，优先级比defaultExpiration高
         */
        private Map<String, Long> expires = new HashMap<>();

        public long getDefaultExpiration() {
            return defaultExpiration;
        }

        public void setDefaultExpiration(long defaultExpiration) {
            this.defaultExpiration = defaultExpiration;
        }

        public Map<String, Long> getExpires() {
            return expires;
        }

        public void setExpires(Map<String, Long> expires) {
            this.expires = expires;
        }
    }
}
