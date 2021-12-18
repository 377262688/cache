package com.cache.demo.config.myCache;

/**
 * @author yangjianzhong
 * @create 2021-11-08 10:18 上午
 **/
public enum CacheMode {

    /**
     * 只开启CAFFEINE 缓存
     */
    ONLY_CAFFEINE("只是用CAFFEINE缓存"),

    /**
     * 只开启二级缓存
     */
    ONLY_REDIS("只是使用REDIS缓存"),

    /**
     * 同时开启CAFFEINE缓存和REDIS缓存
     */
    ALL("同时开启CAFFEINE缓存和REDIS缓存");

    private String label;

    CacheMode(String label) {
        this.label = label;
    }
}
