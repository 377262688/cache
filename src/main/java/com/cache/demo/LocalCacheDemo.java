package com.cache.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangjianzhong
 * @create 2021-03-24 下午2:43
 **/
public class LocalCacheDemo {

    public void UseLocalCache(){
        //一个本地的缓存变量
        Map<String, Object> localCacheStoreMap = new HashMap<String, Object>();

        List<Object> infosList = this.getInfoList();
        for(Object item:infosList){
            if(localCacheStoreMap.containsKey(item)){ //缓存命中 使用缓存数据
                // todo
            } else { // 缓存未命中  IO获取数据，结果存入缓存
                Object valueObject = this.getInfoFromDB();
                localCacheStoreMap.put(valueObject.toString(), valueObject);

            }
        }
    }
    //示例
    private List<Object> getInfoList(){
        return new ArrayList<>();
    }

    //示例数据库IO获取
    private Object getInfoFromDB(){
        return new Object();
    }
}
