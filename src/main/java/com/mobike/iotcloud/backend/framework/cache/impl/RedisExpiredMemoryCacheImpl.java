package com.mobike.iotcloud.backend.framework.cache.impl;


import com.mobike.iotcloud.backend.framework.cache.MemoryCache;

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
    public void set(K key, V val) {

    }

    @Override
    public int del(K key) {
        return 0;
    }
}
