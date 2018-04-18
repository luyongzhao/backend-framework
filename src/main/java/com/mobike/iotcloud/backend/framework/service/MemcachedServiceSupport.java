package com.mobike.iotcloud.backend.framework.service;

import com.mobike.iotcloud.backend.framework.cache.MemoryCache;
import com.mobike.iotcloud.backend.framework.dao.dto.DtoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * 实现memcached懒加载逻辑
 *
 * @author luyongzhao
 */
/**
public abstract class MemcachedServiceSupport extends ServiceSupport {
    private static final Logger log = LoggerFactory.getLogger(MemcachedServiceSupport.class);

    protected <T extends DtoSupport, K extends Serializable> T jsonMemcachedGet(IMemcached memcached, IDatabase db,
                                                                                String keyPrefix, K id, Class<T> cls) {
        return jsonMemcachedGet(memcached, db, keyPrefix, id, cls, null);
    }

    protected <T extends DtoSupport, K extends Serializable> T jsonMemcachedGet(MemoryCach<> memcached, IDatabase db,
                                                                                String keyPrefix, K id, Class<T> cls, String param) {
        String key = keyPrefix + id.toString();

        T t = memcached.getJSON(key, cls);
        if (t == null) {
            if (param == null) {
                t = (T) db.get(id, cls);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("from ").append(cls.getName()).append(" where ").append(param).append("=?");
                List<T> list = db.list(sb.toString(), id);
                if (list != null && list.size() > 0)
                    t = list.get(0);
                else
                    return null;
            }
            if (t != null) {
                memcached.setJSON(key, t, cls);
            }
        }
        return t;
    }

    protected <T extends DtoSupport, K extends Serializable> T jsonMemcachedRefresh(IMemcached memcached, IDatabase db,
                                                                                    String keyPrefix, K id, Class<T> cls) {
        String key = keyPrefix + id.toString();

        T t = (T) db.get(id, cls);
        if (t != null) {
            memcached.setJSON(key, t, cls);
        }
        return t;
    }

    protected <T extends DtoSupport, K extends Serializable> List<T> jsonMemcachedList(IMemcached memcached,
                                                                                       IDatabase db, String keyPrefix, Collection<K> ids, Class<T> cls) {
        return jsonMemcachedList(memcached, db, keyPrefix, ids, cls, null);
    }

    protected <T extends DtoSupport, K extends Serializable> List<T> jsonMemcachedList(IMemcached memcached,
                                                                                       IDatabase db, String keyPrefix, Collection<K> ids, Class<T> cls, String param) {
        List<String> keys = new ArrayList<String>(ids.size());
        for (K id : ids) {
            keys.add(keyPrefix + id.toString());
        }

        Map<String, GetsResponse<T>> cached = memcached.getsJSON(keys, cls);

        List<T> list = new ArrayList<T>(ids.size());

        int idx = 0;
        for (K id : ids) {
            GetsResponse<T> resp = cached.get(keys.get(idx));
            if (resp != null && resp.getValue() != null) {
                list.add(resp.getValue());
            } else {
                T t = jsonMemcachedGet(memcached, db, keyPrefix, id, cls, param);
                if (t != null) {
                    list.add(t);
                }
            }
            idx++;
        }
        return list;
    }

    protected <T extends DtoSupport, K extends Serializable> Map<K, T> jsonMemcachedMap(IMemcached memcached,
                                                                                        IDatabase db, String keyPrefix, Collection<K> ids, Class<T> cls) {
        return jsonMemcachedMap(memcached, db, keyPrefix, ids, cls, null);
    }

    protected <T extends DtoSupport, K extends Serializable> Map<K, T> jsonMemcachedMap(IMemcached memcached,
                                                                                        IDatabase db, String keyPrefix, Collection<K> ids, Class<T> cls, String param) {
        List<String> keys = new ArrayList<String>(ids.size());
        for (K id : ids) {
            keys.add(keyPrefix + id.toString());
        }

        Map<String, GetsResponse<T>> cached = memcached.getsJSON(keys, cls);

        Map<K, T> map = new HashMap<K, T>();

        int i = 0;
        for (K id : ids) {
            GetsResponse<T> resp = cached.get(keys.get(i));

            if (resp != null && resp.getValue() != null) {
                map.put(id, resp.getValue());
            } else {
                T t = jsonMemcachedGet(memcached, db, keyPrefix, id, cls, param);
                if (t != null) {
                    map.put(id, t);
                }
            }
            i++;
        }
        return map;
    }

    // ////////////////java servial

    protected <T extends DtoSupport, K extends Serializable> T memcachedGet(IMemcached memcached, IDatabase db,
                                                                            String keyPrefix, K id, Class<T> cls) {
        String key = keyPrefix + id;

        T t = memcached.get(key);
        if (t == null) {
            t = (T) db.get(id, cls);
            if (t != null) {
                memcached.set(key, t);
            }
        }
        return t;
    }

    protected <T extends DtoSupport, K extends Serializable> List<T> memcachedList(IMemcached memcached, IDatabase db,
                                                                                   String keyPrefix, Collection<K> ids, Class<T> cls) {
        List<String> keys = new ArrayList<String>(ids.size());
        for (K id : ids) {
            keys.add(keyPrefix + id.toString());
        }

        Map<String, GetsResponse<T>> cached = memcached.gets(keys);

        List<T> list = new ArrayList<T>(ids.size());
        int idx = 0;
        for (K id : ids) {
            String key = keys.get(idx);
            GetsResponse<T> resp = cached.get(key);

            if (resp != null && resp.getValue() != null) {
                list.add(resp.getValue());
            } else {
                T t = memcachedGet(memcached, db, keyPrefix, id, cls);
                if (t != null) {
                    list.add(t);
                }
            }
            idx++;
        }
        return list;
    }

    protected <T extends DtoSupport, K extends Serializable> Map<K, T> memcachedMap(IMemcached memcached, IDatabase db,
                                                                                    String keyPrefix, Collection<K> ids, Class<T> cls) {
        List<String> keys = new ArrayList<String>(ids.size());
        for (K id : ids) {
            keys.add(keyPrefix + id);
        }

        Map<String, GetsResponse<T>> cached = memcached.gets(keys);

        Map<K, T> map = new HashMap<K, T>(ids.size());
        int idx = 0;

        for (K id : ids) {
            String key = keys.get(idx);
            GetsResponse<T> resp = cached.get(key);

            if (resp != null && resp.getValue() != null) {
                map.put(id, resp.getValue());
            } else {
                T t = memcachedGet(memcached, db, keyPrefix, id, cls);
                if (t != null) {
                    map.put(id, t);
                }
            }
            idx++;
        }
        return map;
    }


}
 **/