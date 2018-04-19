package com.mobike.iotcloud.backend.framework.cache.impl;


import com.mobike.iotcloud.backend.framework.cache.MemoryCache;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地缓存
 * @param <K>
 * @param <V>
 */
@Service("memoryCache")
public class LocalMemoryCacheImpl<K,V> implements MemoryCache<K,V> {

    private static final CacheManager cacheManager = CacheManager.create(LocalMemoryCacheImpl.class.getResourceAsStream("/ehcache.xml"));

    private static final String cacheName = "localCache";

    @Override
    public V get(K key) {

        Element ele = cache().get(key);
        if (ele == null) {
            return null;
        }

        return (V)ele.getObjectValue();
    }

    @Override
    public Map<K, V> gets(Collection<K> keys) {

        Map<K,V> resultMap = new HashMap<>();

        Map<Object,Element> map = cache().getAll(keys);

        if (map == null || map.isEmpty()) {
            return resultMap;
        }

        //遍历获取所有的实体
        for (Map.Entry<Object, Element> entry : map.entrySet()) {

            V val = null;
            if (entry.getValue() != null) {
                val = (V)entry.getValue().getObjectValue();
            }

            resultMap.put((K)entry.getKey(),val);
        }

        return resultMap;


    }

    @Override
    public void put(K key, V val) {

        Element ele = new Element(key,val);
        cache().put(ele);
    }

    @Override
    public boolean remove(K key) {

        return cache().remove(key);
    }

    private Cache cache(){

        Cache cache = cacheManager.getCache(cacheName);

        return cache;
    }
}
