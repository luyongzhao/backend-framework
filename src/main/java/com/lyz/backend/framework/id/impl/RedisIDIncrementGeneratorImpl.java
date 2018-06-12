package com.lyz.backend.framework.id.impl;


import com.lyz.backend.framework.cache.PersistentCache;
import com.lyz.backend.framework.id.AbstractRedisIDGenerator;

/**
 * 通过redis实现的自增id生成器<br />
 * <p>
 * <pre>
 * protected Integer cacheSize;// 本地缓存数量大小
 * protected String key;// redis的key值,如果key不存在将在init方法中自动创建
 * protected Long initValue;// 初始值
 * protected Redis redis;// redis引用
 * </pre>
 *
 * @author luyongzhao
 */
public class RedisIDIncrementGeneratorImpl extends AbstractRedisIDGenerator {

    protected Long initValue = 100L;

    private long nextId = 0L;
    private long maxId = 0L;

    public RedisIDIncrementGeneratorImpl(){

    }


    public RedisIDIncrementGeneratorImpl(String key, int cacheSize, long initValue, PersistentCache persistentCache){

        super.key = key;
        super.persistentCache = persistentCache;
        super.cacheSize = cacheSize;
        this.initValue = initValue;
    }



    public RedisIDIncrementGeneratorImpl init() {
        super.init();

        if (!persistentCache.exists(key)) {
            nextId = initValue - 1;
            maxId = initValue + cacheSize;

            persistentCache.set(key, String.valueOf(maxId));
        }

        return this;
    }

    public String nextStringID() {
        String prefix = prefix();
        if (prefix != null) {
            return prefix + String.valueOf(nextLongID());
        } else {
            return String.valueOf(nextLongID());
        }
    }


    public synchronized Long nextLongID() {
        if (nextId == maxId) {
            maxId = persistentCache.incrBy(key, cacheSize);
            nextId = maxId - cacheSize + 1;
        } else {
            nextId++;
        }
        return nextId;
    }

    public Long getInitValue() {
        return initValue;
    }

    public void setInitValue(Long initValue) {
        this.initValue = initValue;
    }
}
