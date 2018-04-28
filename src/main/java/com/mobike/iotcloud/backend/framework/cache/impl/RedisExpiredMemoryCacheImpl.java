package com.mobike.iotcloud.backend.framework.cache.impl;


import com.mobike.iotcloud.backend.framework.cache.MemoryCache;
import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import com.mobike.iotcloud.backend.framework.util.GenericsUtil;
import com.mobike.iotcloud.backend.framework.util.JsonUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于redis的带有过期时间的缓存
 * @param <K>
 * @param <V>
 */
@Data
@Service("redisExpiredMemoryCache")
public class RedisExpiredMemoryCacheImpl<K,V> implements MemoryCache<K,V> {

    private int expiredAfterSeconds = 3600;

    @Autowired
    private PersistentCache persistentCache;

    @Override
    public V get(K key) {

        if (key == null) {
            return null;
        }

        String value = persistentCache.get(key.toString());
        if (value == null) {
            return null;
        }

        //获取值的类型
        Class<V> clazz = GenericsUtil.getSuperClassGenricType(RedisExpiredMemoryCacheImpl.class,1);

        return JsonUtil.parseJSONObject(value, clazz);
    }

    //TODO:单个获取存在性能问题
    @Override
    public Map<K, V> gets(final Collection<K> keys) {

        if (keys==null || keys.isEmpty()) {
            return new HashMap<>(0);
        }

        Map<K,V> map = new HashMap<>();


        List<String> list = persistentCache.pipelined(new PipelineBlock() {
            @Override
            public void execute() {
                for(K k : keys){
                    Response<String> resp = get(k.toString());
                }
            }
        });


        int index = 0;
        //获取值的类型
        Class<V> clazz = GenericsUtil.getSuperClassGenricType(RedisExpiredMemoryCacheImpl.class,1);

        for (K key : keys) {

            String value = list.get(index);
            index++;

            if (value == null) {
                continue;
            }

            map.put(key,JsonUtil.parseJSONObject(value, clazz));
        }

        /**
        for (K key : keys) {
            V v = get(key);
            if (v == null) {
                continue;
            }

            map.put(key,v);
        }**/

        return map;

    }

    @Override
    public void put(K key, V val) {

        persistentCache.setex(key.toString(),expiredAfterSeconds,JsonUtil.toJSONString(val));

    }

    @Override
    public boolean remove(K key) {

        Long count = persistentCache.del(key.toString());

        if (count == null) {
            return false;
        }
        return true;
    }
}
