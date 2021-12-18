package com.cache.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * @author yangjianzhong
 * @create 2021-03-24 下午3:31
 **/
public class GuavaCahceDemo {


    private LoadingCache<String, String> loadingCache =
        CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                System.out.println("key:" + key);
                if ("key".equals(key)) {
                    return "key return result";
                } else {
                    return "get-if-absent-compute";
                }
            }
        });

    private Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();


    public static void main(String[] args) throws ExecutionException {
        GuavaCahceDemo guavaCahceDemo = new GuavaCahceDemo();
        guavaCahceDemo.loadingCache();
        guavaCahceDemo.callablex();
    }


    /**
     * CacheLoader
     */
    public void loadingCache() {

        String resultVal = null;
        try {
            resultVal = loadingCache.get("key");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(resultVal);
    }

    /**
     * Callable
     */
    public void callablex() throws ExecutionException {
        String result = cache.get("key", () -> "result");
        System.out.println(result);
    }
}
