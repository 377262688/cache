package com.cache.demo;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author yangjianzhong
 * @create 2021-03-24 下午2:40
 **/
public class EHCacheDemo {

    public static void main(String[] args) {
        final CacheManager cacheManager = new CacheManager();
        final Cache cache1 = cacheManager.getCache("CACHE1");
        final Cache cache2 = cacheManager.getCache("CACHE2");
        final String key1 = "key1";
        final Element putGreeting1 = new Element(key1, "Hello, World!");
        cache1.put(putGreeting1);
        final Element getGreeting1 = cache1.get(key1);
        System.out.println("cahce1:" + getGreeting1.getObjectValue());

        final Element putGreeting2 = new Element(key1, "Hello, World!");
        cache2.put(putGreeting2);
        final Element getGreeting2 = cache2.get(key1);
        System.out.println("cache2:" + getGreeting2.getObjectValue());

        String key2 = "key2";
        final Element putGreeting3 = new Element(key2, "cache1: Hello, Worldxxxx!");
        cache1.put(putGreeting3);
        final Element getGreeting3 = cache1.get(key2);

        final Element putGreeting4 = new Element(key2, "cache2: Hello, World!");
        cache2.put(putGreeting4);
        final Element getGreeting4 = cache2.get(key2);

        System.out.println("cahce1:" + getGreeting3.getObjectValue());
        System.out.println("cache2:" + getGreeting4.getObjectValue());
    }
}
