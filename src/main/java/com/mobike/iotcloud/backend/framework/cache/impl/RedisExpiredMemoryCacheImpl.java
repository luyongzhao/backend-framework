package com.mobike.iotcloud.backend.framework.cache.impl;


import com.mobike.iotcloud.backend.framework.cache.MemoryCache;

import java.util.Collection;
import java.util.Map;

/**
 * 基于redis的带有过期时间的缓存
 * @param <K>
 * @param <V>
 */
public class RedisExpiredMemoryCacheImpl<K,V> implements MemoryCache<K,V> {


    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public Map<K, V> gets(Collection<K> keys) {

        return null;
    }

    @Override
    public void put(K key, V val) {

    }

    @Override
    public boolean remove(K key) {

        return false;
    }
}
