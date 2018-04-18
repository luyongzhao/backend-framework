package com.mobike.iotcloud.backend.framework.cache;

/**
 * 内存缓存封装，缓存中的数据被清除之后不影响业务逻辑
 */
public interface MemoryCache<K,V> {

    /**
     * 获取缓存内容
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 设置缓存
     * @param key
     * @param val
     */
    void set(K key, V val);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    int del(K key);

}
