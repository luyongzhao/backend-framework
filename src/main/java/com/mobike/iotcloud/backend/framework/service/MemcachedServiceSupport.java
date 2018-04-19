package com.mobike.iotcloud.backend.framework.service;

import com.mobike.iotcloud.backend.framework.cache.MemoryCache;
import com.mobike.iotcloud.backend.framework.dao.BasicDao;
import com.mobike.iotcloud.backend.framework.dao.dto.DtoSupport;

import java.io.Serializable;
import java.util.*;

/**
 * 实现memcached懒加载逻辑
 *
 * @author luyongzhao
 */
public abstract class MemcachedServiceSupport extends ServiceSupport {


    protected <T extends DtoSupport, K extends Serializable> T memcachedDelete(MemoryCache<String, T> memcached, BasicDao basicDao, String keyPrefix, T t, K id) {

        String key = keyPrefix + id.toString();

        //删除数据库中的数据
        basicDao.delete(t);

        //删除缓存
        memcached.remove(key);

        return t;
    }

    protected <T extends DtoSupport, K extends Serializable> T memcachedUpdate(MemoryCache<String, T> memcached, BasicDao basicDao, String keyPrefix, T t, K id) {

        String key = keyPrefix + id.toString();

        //更新数据到数据库
        basicDao.update(t);

        //保存数据到缓存
        memcached.put(key, t);

        return t;
    }

    protected <T extends DtoSupport, K extends Serializable> T memcachedSave(MemoryCache<String, T> memcached, BasicDao basicDao, String keyPrefix, T t, K id) {

        String key = keyPrefix + id.toString();

        //保存数据到数据库
        basicDao.save(t);

        //保存数据到缓存
        memcached.put(key, t);

        return t;
    }

    protected <T extends DtoSupport, K extends Serializable> T memcachedGet(MemoryCache<String, T> memcached, BasicDao basicDao, String keyPrefix, K id, Class<T> cls) {

        String key = keyPrefix + id.toString();

        //尝试从缓存获取
        T t = memcached.get(key);

        //缓存不存在则去数据库获取
        if (t == null) {
            t = (T) basicDao.get(id,cls);
            //数据库存在，则加载入缓存
            if (t != null) {
                memcached.put(key, t);
            }
        }
        return t;
    }


    protected <T extends DtoSupport, K extends Serializable> T memcachedRefresh(MemoryCache<String, T> memcached, BasicDao basicDao,
                                                                                    String keyPrefix, K id, Class<T> cls) {
        String key = keyPrefix + id.toString();

        T t = (T) basicDao.get(id,cls);
        if (t != null) {
            memcached.put(key, t);
        }
        return t;
    }


    protected <T extends DtoSupport, K extends Serializable> List<T> memcachedList(MemoryCache<String, T> memcached,
                                                                                       BasicDao basicDao, String keyPrefix, Collection<K> ids, Class<T> cls) {
        List<String> keys = new ArrayList<String>(ids.size());
        for (K id : ids) {
            keys.add(keyPrefix + id.toString());
        }

        Map<String, T> cached = memcached.gets(keys);

        List<T> list = new ArrayList<T>(ids.size());

        int idx = 0;
        for (K id : ids) {
            T resp = cached.get(keys.get(idx));
            if ( resp != null ) {
                list.add(resp);
            } else {//缓存中不存在则尝试去数据库中获取
                T t = memcachedRefresh(memcached, basicDao, keyPrefix, id, cls);
                if (t != null) {
                    list.add(t);
                }
            }
            idx++;
        }
        return list;
    }

    protected <T extends DtoSupport, K extends Serializable> Map<K, T> memcachedMap(MemoryCache<String, T> memcached,
                                                                                        BasicDao basicDao, String keyPrefix, Collection<K> ids, Class<T> cls) {
        List<String> keys = new ArrayList<String>(ids.size());
        for (K id : ids) {
            keys.add(keyPrefix + id.toString());
        }

        Map<String, T> cached = memcached.gets(keys);

        Map<K, T> map = new HashMap<K, T>();

        int i = 0;
        for (K id : ids) {
            T resp = cached.get(keys.get(i));

            if ( resp != null ) {
                map.put(id, resp);
            } else {//缓存中不存在则尝试去数据库中获取
                T t = memcachedRefresh(memcached, basicDao, keyPrefix, id, cls);
                if (t != null) {
                    map.put(id, t);
                }
            }
            i++;
        }
        return map;
    }

}
