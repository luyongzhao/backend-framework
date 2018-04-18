package com.mobike.iotcloud.backend.framework.cache.impl;


import com.mobike.iotcloud.backend.framework.cache.MemoryCache;

/**
 * 本地缓存
 * @param <K>
 * @param <V>
 */
public class LocalMemoryCacheImpl<K,V> implements MemoryCache<K,V> {


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
