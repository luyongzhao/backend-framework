package com.mobike.iotcloud.backend.framework.cache.impl;

import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import com.mobike.iotcloud.backend.framework.util.JavaBeanSerializerUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

import java.util.*;

/**
 * 一次使用一个命令，从连接池中打开连接，执行操作，返回链接，如果一个业务逻辑中要执行多个命令， 建议不要使用本类<br />
 *
 * @author luyongzhao
 */
@Service("persistentCache")
public class RedisPersistentCacheImpl implements PersistentCache {

    private Pool<Jedis> pool = null;

    public RedisPersistentCacheImpl(){

    }

    public RedisPersistentCacheImpl(Pool<Jedis> pool){

        this.pool = pool;
    }

    @Override
    public int lreplaceReturnLength(String key, String oldValue, String newValue) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            int count = 0;

            List<String> list = jedis.lrange(key, 0, -1);
            if (list != null && list.size() > 0) {
                int idx = -1;
                for (int i = 0; i < list.size(); i++) {
                    String v = list.get(i);
                    if (StringUtils.equals(v, oldValue)) {
                        idx = i;
                    } else if (!StringUtils.equals(v, newValue)) {
                        count++;
                    }
                }

                if (idx != -1) {
                    jedis.lset(key, idx, newValue);
                }
            }

            return count;
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    public boolean lreplace(String key, String oldValue, String newValue) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            List<String> list = jedis.lrange(key, 0, -1);
            if (list != null && list.size() > 0) {
                int idx = -1;
                for (int i = 0; i < list.size(); i++) {
                    String v = list.get(i);
                    if (StringUtils.equals(v, oldValue)) {
                        idx = i;
                        break;
                    }
                }

                if (idx != -1) {
                    jedis.lset(key, idx, newValue);
                    return true;
                }
            }

            return false;
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
     * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hoset(java.lang.String,
	 * java.lang.Object)
	 */
    @Override
    public String hoset(String key, Object bean) {
        return hmset(key, JavaBeanSerializerUtil.describe(bean));
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#ping()
	 */
    @Override
    public String ping() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ping();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#set(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public String set(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#get(java.lang.String)
	 */
    @Override
    public String get(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getInt(java.lang.String)
	 */
    @Override
    public Integer getInt(String key) {
        return NumberUtils.toInt(this.get(key), 0);
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#set(byte[], byte[])
	 */
    @Override
    public String set(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#quit()
	 */
    @Override
    public String quit() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.quit();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#exists(java.lang.String)
	 */
    @Override
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#get(byte[])
	 */
    @Override
    public byte[] get(byte[] key) {

        Jedis jedis = null;
        try {

            jedis = pool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#del(java.lang.String)
	 */
    @Override
    public Long del(String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#exists(byte[])
	 */
    @Override
    public Boolean exists(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#type(java.lang.String)
	 */
    @Override
    public String type(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.type(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#flushDB()
	 */
    @Override
    public String flushDB() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.flushDB();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#del(byte)
	 */
    @Override
    public Long del(byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#keys(java.lang.String)
	 */
    @Override
    public Set<String> keys(String pattern) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(pattern);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#type(byte[])
	 */
    @Override
    public String type(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.type(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#randomKey()
	 */
    @Override
    public String randomKey() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.randomKey();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#keys(byte[])
	 */
    @Override
    public Set<byte[]> keys(byte[] pattern) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(pattern);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rename(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public String rename(String oldkey, String newkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rename(oldkey, newkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#renamenx(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long renamenx(String oldkey, String newkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.renamenx(oldkey, newkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#randomBinaryKey()
	 */
    @Override
    public byte[] randomBinaryKey() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.randomBinaryKey();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rename(byte[], byte[])
	 */
    @Override
    public String rename(byte[] oldkey, byte[] newkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rename(oldkey, newkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#expire(java.lang.String, int)
	 */
    @Override
    public Long expire(String key, int seconds) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.expire(key, seconds);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#renamenx(byte[], byte[])
	 */
    @Override
    public Long renamenx(byte[] oldkey, byte[] newkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.renamenx(oldkey, newkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#expireAt(java.lang.String, long)
	 */
    @Override
    public Long expireAt(String key, long unixTime) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.expireAt(key, unixTime);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#dbSize()
	 */
    @Override
    public Long dbSize() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.dbSize();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#ttl(java.lang.String)
	 */
    @Override
    public Long ttl(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#expire(byte[], int)
	 */
    @Override
    public Long expire(byte[] key, int seconds) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.expire(key, seconds);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#select(int)
	 */
    @Override
    public String select(int index) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.select(index);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#move(java.lang.String, int)
	 */
    @Override
    public Long move(String key, int dbIndex) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.move(key, dbIndex);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#expireAt(byte[], long)
	 */
    @Override
    public Long expireAt(byte[] key, long unixTime) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.expireAt(key, unixTime);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#flushAll()
	 */
    @Override
    public String flushAll() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.flushAll();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#ttl(byte[])
	 */
    @Override
    public Long ttl(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getSet(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public String getSet(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getSet(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#mget(java.lang.String)
	 */
    @Override
    public List<String> mget(String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.mget(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#mgetInt(java.lang.String)
	 */
    @Override
    public List<Integer> mgetInt(String... keys) {
        List<String> mget = this.mget(keys);
        if (mget != null && mget.size() == keys.length) {
            List<Integer> mgetInt = new ArrayList<Integer>(mget.size());
            for (String s : mget) {
                mgetInt.add(NumberUtils.toInt(s));
            }
            return mgetInt;
        } else {
            return null;
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#move(byte[], int)
	 */
    @Override
    public Long move(byte[] key, int dbIndex) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.move(key, dbIndex);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setnx(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long setnx(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setnx(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getSet(byte[], byte[])
	 */
    @Override
    public byte[] getSet(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getSet(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setex(java.lang.String, int,
	 * java.lang.String)
	 */
    @Override
    public String setex(String key, int seconds, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key, seconds, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#mget(byte)
	 */
    @Override
    public List<byte[]> mget(byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.mget(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#mset(java.lang.String)
	 */
    @Override
    public String mset(String... keysvalues) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.mset(keysvalues);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setnx(byte[], byte[])
	 */
    @Override
    public Long setnx(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setnx(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#msetnx(java.lang.String)
	 */
    @Override
    public Long msetnx(String... keysvalues) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.msetnx(keysvalues);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setex(byte[], int, byte[])
	 */
    @Override
    public String setex(byte[] key, int seconds, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key, seconds, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#decrBy(java.lang.String, long)
	 */
    @Override
    public Long decrBy(String key, long integer) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key, integer);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#mset(byte)
	 */
    @Override
    public String mset(byte[]... keysvalues) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.mset(keysvalues);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#decr(java.lang.String)
	 */
    @Override
    public Long decr(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#incrBy(java.lang.String, long)
	 */
    @Override
    public Long incrBy(String key, long integer) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrBy(key, integer);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#msetnx(byte)
	 */
    @Override
    public Long msetnx(byte[]... keysvalues) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.msetnx(keysvalues);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#incr(java.lang.String)
	 */
    @Override
    public Long incr(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#decrBy(byte[], long)
	 */
    @Override
    public Long decrBy(byte[] key, long integer) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key, integer);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#append(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long append(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.append(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#decr(byte[])
	 */
    @Override
    public Long decr(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#substr(java.lang.String, int, int)
	 */
    @Override
    public String substr(String key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.substr(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#incrBy(byte[], long)
	 */
    @Override
    public Long incrBy(byte[] key, long integer) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrBy(key, integer);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hset(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Long hset(String key, String field, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hset(key, field, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#incr(byte[])
	 */
    @Override
    public Long incr(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#append(byte[], byte[])
	 */
    @Override
    public Long append(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.append(key, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hget(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public String hget(String key, String field) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#substr(byte[], int, int)
	 */
    @Override
    public byte[] substr(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.substr(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hsetnx(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Long hsetnx(String key, String field, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hsetnx(key, field, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hset(byte[], byte[], byte[])
	 */
    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hset(key, field, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hmset(java.lang.String,
	 * java.util.Map)
	 */
    @Override
    public String hmset(String key, Map<String, String> hash) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, hash);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hget(byte[], byte[])
	 */
    @Override
    public byte[] hget(byte[] key, byte[] field) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hmget(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public List<String> hmget(String key, String... fields) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, fields);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hsetnx(byte[], byte[], byte[])
	 */
    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hsetnx(key, field, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hincrBy(java.lang.String,
	 * java.lang.String, long)
	 */
    @Override
    public Long hincrBy(String key, String field, long value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrBy(key, field, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hmset(byte[], java.util.Map)
	 */
    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, hash);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hexists(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Boolean hexists(String key, String field) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hexists(key, field);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hmget(byte[], byte)
	 */
    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, fields);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hdel(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long hdel(String key, String... fields) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hdel(key, fields);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hincrBy(byte[], byte[], long)
	 */
    @Override
    public Long hincrBy(byte[] key, byte[] field, long value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrBy(key, field, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hlen(java.lang.String)
	 */
    @Override
    public Long hlen(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hlen(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hexists(byte[], byte[])
	 */
    @Override
    public Boolean hexists(byte[] key, byte[] field) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hexists(key, field);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hkeys(java.lang.String)
	 */
    @Override
    public Set<String> hkeys(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hkeys(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hdel(byte[], byte)
	 */
    @Override
    public Long hdel(byte[] key, byte[]... fields) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hdel(key, fields);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hvals(java.lang.String)
	 */
    @Override
    public List<String> hvals(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hvals(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hlen(byte[])
	 */
    @Override
    public Long hlen(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hlen(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hgetAll(java.lang.String)
	 */
    @Override
    public Map<String, String> hgetAll(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hgetAll(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hgetAll(java.lang.String,
	 * java.lang.Class)
	 */
    @Override
    public <T> T hgetAll(String key, Class<T> cls) {
        Map<String, String> map = this.hgetAll(key);
        if (map != null && map.size() > 0) {
            return JavaBeanSerializerUtil.map2Object(map, cls);
        }

        return null;
    }

    @Override
    public <T> T hgetAll(final String key, Class<T> cls, final String... fileds) {
        List<String> args = this.pipelined(new PipelineBlock() {
            @Override
            public void execute() {
                for (String f : fileds) {
                    hget(key, f);
                }
            }
        });

        Map<String, String> map = new HashMap<String, String>(fileds.length);

        int idx = 0;
        for (String v : args) {
            map.put(fileds[idx], v);
            idx++;
        }

        return JavaBeanSerializerUtil.map2Object(map, cls);
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hkeys(byte[])
	 */
    @Override
    public Set<byte[]> hkeys(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hkeys(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpush(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long rpush(String key, String... strings) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpush(key, strings);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hvals(byte[])
	 */
    @Override
    public List<byte[]> hvals(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hvals(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lpush(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long lpush(String key, String... strings) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, strings);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#hgetAll(byte[])
	 */
    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hgetAll(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#llen(java.lang.String)
	 */
    @Override
    public Long llen(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.llen(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrange(java.lang.String)
	 */
    @Override
    public List<String> lrange(String key) {
        return this.lrange(key, 0, -1);
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrange(java.lang.String, long,
	 * long)
	 */
    @Override
    public List<String> lrange(String key, long start, long end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrangeInteger(java.lang.String)
	 */
    @Override
    public List<Integer> lrangeInteger(String key) {
        return this.lrangeInteger(key, 0, -1);
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrangeInteger(java.lang.String,
	 * long, long)
	 */
    @Override
    public List<Integer> lrangeInteger(String key, long start, long end) {
        List<String> list = this.lrange(key, start, end);
        if (list != null && list.size() > 0) {
            List<Integer> listi = new ArrayList<Integer>(list.size());
            for (String v : list) {
                listi.add(Integer.valueOf(v));
            }
            return listi;
        }
        return new ArrayList<Integer>();
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrangeInteger(java.lang.String,
	 * long)
	 */
    @Override
    public List<Integer> lrangeInteger(String key, long start) {
        return this.lrangeInteger(key, start, -1);
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#ltrim(java.lang.String, long, long)
	 */
    @Override
    public String ltrim(String key, long start, long end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ltrim(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpush(byte[], byte)
	 */
    @Override
    public Long rpush(byte[] key, byte[]... strings) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpush(key, strings);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lindex(java.lang.String, long)
	 */
    @Override
    public String lindex(String key, long index) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lindex(key, index);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lpush(byte[], byte)
	 */
    @Override
    public Long lpush(byte[] key, byte[]... strings) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, strings);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lset(java.lang.String, long,
	 * java.lang.String)
	 */
    @Override
    public String lset(String key, long index, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lset(key, index, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#llen(byte[])
	 */
    @Override
    public Long llen(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.llen(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrange(byte[], int, int)
	 */
    @Override
    public List<byte[]> lrange(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrem(java.lang.String, long,
	 * java.lang.String)
	 */
    @Override
    public Long lrem(String key, long count, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrem(key, count, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#ltrim(byte[], int, int)
	 */
    @Override
    public String ltrim(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ltrim(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lpop(java.lang.String)
	 */
    @Override
    public String lpop(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    public List<String> lpopBatch(final String key, final int max) {
        return pipelined(new PipelineBlock() {
            @Override
            public void execute() {
                for (int i = 0; i < max; i++) {
                    lpop(key);
                }
            }
        });
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpop(java.lang.String)
	 */
    @Override
    public String rpop(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    public List<String> rpopBatch(final String key, final int max) {
        return pipelined(new PipelineBlock() {
            @Override
            public void execute() {
                for (int i = 0; i < max; i++) {
                    rpop(key);
                }
            }
        });
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lindex(byte[], int)
	 */
    @Override
    public byte[] lindex(byte[] key, int index) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lindex(key, index);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpoplpush(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public String rpoplpush(String srckey, String dstkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpoplpush(srckey, dstkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lset(byte[], int, byte[])
	 */
    @Override
    public String lset(byte[] key, int index, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lset(key, index, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sadd(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long sadd(String key, String... members) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, members);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lrem(byte[], int, byte[])
	 */
    @Override
    public Long lrem(byte[] key, int count, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrem(key, count, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#smembers(java.lang.String)
	 */
    @Override
    public Set<String> smembers(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smembers(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lpop(byte[])
	 */
    @Override
    public byte[] lpop(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#srem(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long srem(String key, String... members) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, members);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpop(byte[])
	 */
    @Override
    public byte[] rpop(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpoplpush(byte[], byte[])
	 */
    @Override
    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpoplpush(srckey, dstkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#spop(java.lang.String)
	 */
    @Override
    public String spop(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#smove(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Long smove(String srckey, String dstkey, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smove(srckey, dstkey, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sadd(byte[], byte)
	 */
    @Override
    public Long sadd(byte[] key, byte[]... members) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, members);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#smembers(byte[])
	 */
    @Override
    public Set<byte[]> smembers(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smembers(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scard(java.lang.String)
	 */
    @Override
    public Long scard(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sismember(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Boolean sismember(String key, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#srem(byte[], byte)
	 */
    @Override
    public Long srem(byte[] key, byte[]... member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sinter(java.lang.String)
	 */
    @Override
    public Set<String> sinter(String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinter(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#spop(byte[])
	 */
    @Override
    public byte[] spop(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#smove(byte[], byte[], byte[])
	 */
    @Override
    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smove(srckey, dstkey, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sinterstore(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long sinterstore(String dstkey, String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinterstore(dstkey, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scard(byte[])
	 */
    @Override
    public Long scard(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sunion(java.lang.String)
	 */
    @Override
    public Set<String> sunion(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunion(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sismember(byte[], byte[])
	 */
    @Override
    public Boolean sismember(byte[] key, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sunionstore(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long sunionstore(String dstkey, String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunionstore(dstkey, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sinter(byte)
	 */
    @Override
    public Set<byte[]> sinter(byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinter(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sdiff(java.lang.String)
	 */
    @Override
    public Set<String> sdiff(String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiff(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sinterstore(byte[], byte)
	 */
    @Override
    public Long sinterstore(byte[] dstkey, byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinterstore(dstkey, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sdiffstore(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long sdiffstore(String dstkey, String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiffstore(dstkey, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sunion(byte)
	 */
    @Override
    public Set<byte[]> sunion(byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunion(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#srandmember(java.lang.String)
	 */
    @Override
    public String srandmember(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srandmember(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zadd(java.lang.String, double,
	 * java.lang.String)
	 */
    @Override
    public Long zadd(String key, double score, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sunionstore(byte[], byte)
	 */
    @Override
    public Long sunionstore(byte[] dstkey, byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunionstore(dstkey, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zadd(java.lang.String,
	 * java.util.Map)
	 */
    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, scoreMembers);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sdiff(byte)
	 */
    @Override
    public Set<byte[]> sdiff(byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiff(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrange(java.lang.String, long,
	 * long)
	 */
    @Override
    public Set<String> zrange(String key, long start, long end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sdiffstore(byte[], byte)
	 */
    @Override
    public Long sdiffstore(byte[] dstkey, byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiffstore(dstkey, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#srandmember(byte[])
	 */
    @Override
    public byte[] srandmember(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srandmember(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrem(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long zrem(String key, String... members) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, members);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zadd(byte[], double, byte[])
	 */
    @Override
    public Long zadd(byte[] key, double score, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zincrby(java.lang.String, double,
	 * java.lang.String)
	 */
    @Override
    public Double zincrby(String key, double score, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zincrby(key, score, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zadd(byte[], java.util.Map)
	 */
    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, scoreMembers);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrank(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long zrank(String key, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrank(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrange(byte[], int, int)
	 */
    @Override
    public Set<byte[]> zrange(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrank(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long zrevrank(String key, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrank(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrange(java.lang.String, long,
	 * long)
	 */
    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrem(byte[], byte)
	 */
    @Override
    public Long zrem(byte[] key, byte[]... members) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, members);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zincrby(byte[], double, byte[])
	 */
    @Override
    public Double zincrby(byte[] key, double score, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zincrby(key, score, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeWithScores(java.lang.String,
	 * long, long)
	 */
    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeWithScores(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrank(byte[], byte[])
	 */
    @Override
    public Long zrank(byte[] key, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrank(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrevrangeWithScores(java.lang.String,
	 * long, long)
	 */
    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeWithScores(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrank(byte[], byte[])
	 */
    @Override
    public Long zrevrank(byte[] key, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrank(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zcard(java.lang.String)
	 */
    @Override
    public Long zcard(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrange(byte[], int, int)
	 */
    @Override
    public Set<byte[]> zrevrange(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zscore(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Double zscore(String key, String member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeWithScores(byte[], int, int)
	 */
    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeWithScores(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#watch(java.lang.String)
	 */
    @Override
    public String watch(String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.watch(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(java.lang.String)
	 */
    @Override
    public List<String> sort(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeWithScores(byte[], int,
	 * int)
	 */
    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeWithScores(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(java.lang.String,
	 * redis.clients.jedis.SortingParams)
	 */
    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key, sortingParameters);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zcard(byte[])
	 */
    @Override
    public Long zcard(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#blpop(int, java.lang.String)
	 */
    @Override
    public List<String> blpop(int timeout, String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.blpop(timeout, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zscore(byte[], byte[])
	 */
    @Override
    public Double zscore(byte[] key, byte[] member) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#multi(redis.clients.jedis.TransactionBlock
	 * )
	 */
    @Override
    public List<Object> multi(TransactionBlock jedisTransaction) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.multi(jedisTransaction);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(java.lang.String,
	 * redis.clients.jedis.SortingParams, java.lang.String)
	 */
    @Override
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key, sortingParameters, dstkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long sort(String key, String dstkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key, dstkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#brpop(int, java.lang.String)
	 */
    @Override
    public List<String> brpop(int timeout, String... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#watch(byte)
	 */
    @Override
    public String watch(byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.watch(keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#unwatch()
	 */
    @Override
    public String unwatch() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.unwatch();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#auth(java.lang.String)
	 */
    @Override
    public String auth(String password) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.auth(password);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(byte[])
	 */
    @Override
    public List<byte[]> sort(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(byte[],
	 * redis.clients.jedis.SortingParams)
	 */
    @Override
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key, sortingParameters);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#blpop(int, byte)
	 */
    @Override
    public List<byte[]> blpop(int timeout, byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.blpop(timeout, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#publish(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long publish(String channel, String message) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.publish(channel, message);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#psubscribe(redis.clients.jedis.JedisPubSub
	 * , java.lang.String)
	 */
    @Override
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.psubscribe(jedisPubSub, patterns);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zcount(java.lang.String, double,
	 * double)
	 */
    @Override
    public Long zcount(String key, double min, double max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcount(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(byte[],
	 * redis.clients.jedis.SortingParams, byte[])
	 */
    @Override
    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key, sortingParameters, dstkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zcount(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Long zcount(String key, String min, String max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcount(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#sort(byte[], byte[])
	 */
    @Override
    public Long sort(byte[] key, byte[] dstkey) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sort(key, dstkey);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(java.lang.String,
	 * double, double)
	 */
    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#brpop(int, byte)
	 */
    @Override
    public List<byte[]> brpop(int timeout, byte[]... keys) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, keys);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(java.lang.String,
	 * double, double, int, int)
	 */
    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(java.lang.String,
	 * java.lang.String, java.lang.String, int, int)
	 */
    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#pipelined(redis.clients.jedis.PipelineBlock
	 * )
	 */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> pipelined(PipelineBlock jedisPipeline) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            return (List<T>) jedis.pipelined(jedisPipeline);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrangeByScoreWithScores(java.lang.String
	 * , double, double)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrangeByScoreWithScores(java.lang.String
	 * , java.lang.String, java.lang.String)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrangeByScoreWithScores(java.lang.String
	 * , double, double, int, int)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrangeByScoreWithScores(java.lang.String
	 * , java.lang.String, java.lang.String, int, int)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zcount(byte[], double, double)
	 */
    @Override
    public Long zcount(byte[] key, double min, double max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcount(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zcount(byte[], byte[], byte[])
	 */
    @Override
    public Long zcount(byte[] key, byte[] min, byte[] max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcount(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(byte[], double,
	 * double)
	 */
    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(byte[], byte[],
	 * byte[])
	 */
    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(java.lang.String,
	 * double, double)
	 */
    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(byte[], double,
	 * double, int, int)
	 */
    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScore(byte[], byte[],
	 * byte[], int, int)
	 */
    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(java.lang.String,
	 * double, double, int, int)
	 */
    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScoreWithScores(byte[],
	 * double, double)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrevrangeByScoreWithScores(java.lang
	 * .String, double, double)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScoreWithScores(byte[],
	 * byte[], byte[])
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrevrangeByScoreWithScores(java.lang
	 * .String, double, double, int, int)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScoreWithScores(byte[],
	 * double, double, int, int)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrangeByScoreWithScores(byte[],
	 * byte[], byte[], int, int)
	 */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrevrangeByScoreWithScores(java.lang
	 * .String, java.lang.String, java.lang.String, int, int)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(java.lang.String,
	 * java.lang.String, java.lang.String, int, int)
	 */
    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#zrevrangeByScoreWithScores(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(byte[], double,
	 * double)
	 */
    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zremrangeByRank(java.lang.String,
	 * long, long)
	 */
    @Override
    public Long zremrangeByRank(String key, long start, long end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByRank(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(byte[], byte[],
	 * byte[])
	 */
    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zremrangeByScore(java.lang.String,
	 * double, double)
	 */
    @Override
    public Long zremrangeByScore(String key, double start, double end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByScore(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(byte[], double,
	 * double, int, int)
	 */
    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zremrangeByScore(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public Long zremrangeByScore(String key, String start, String end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByScore(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScore(byte[], byte[],
	 * byte[], int, int)
	 */
    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zunionstore(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long zunionstore(String dstkey, String... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zunionstore(dstkey, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScoreWithScores(byte[],
	 * double, double)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zunionstore(java.lang.String,
	 * redis.clients.jedis.ZParams, java.lang.String)
	 */
    @Override
    public Long zunionstore(String dstkey, ZParams params, String... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zunionstore(dstkey, params, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScoreWithScores(byte[],
	 * double, double, int, int)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zinterstore(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long zinterstore(String dstkey, String... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zinterstore(dstkey, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScoreWithScores(byte[],
	 * byte[], byte[])
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zinterstore(java.lang.String,
	 * redis.clients.jedis.ZParams, java.lang.String)
	 */
    @Override
    public Long zinterstore(String dstkey, ZParams params, String... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zinterstore(dstkey, params, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#strlen(java.lang.String)
	 */
    @Override
    public Long strlen(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.strlen(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zrevrangeByScoreWithScores(byte[],
	 * byte[], byte[], int, int)
	 */
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lpushx(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long lpushx(String key, String string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpushx(key, string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#persist(java.lang.String)
	 */
    @Override
    public Long persist(String key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.persist(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zremrangeByRank(byte[], int, int)
	 */
    @Override
    public Long zremrangeByRank(byte[] key, int start, int end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByRank(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpushx(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long rpushx(String key, String string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpushx(key, string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zremrangeByScore(byte[], double,
	 * double)
	 */
    @Override
    public Long zremrangeByScore(byte[] key, double start, double end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByScore(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#echo(java.lang.String)
	 */
    @Override
    public String echo(String string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.echo(string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zremrangeByScore(byte[], byte[],
	 * byte[])
	 */
    @Override
    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByScore(key, start, end);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#linsert(java.lang.String,
	 * redis.clients.jedis.BinaryClient.LIST_POSITION, java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.linsert(key, where, pivot, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zunionstore(byte[], byte)
	 */
    @Override
    public Long zunionstore(byte[] dstkey, byte[]... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zunionstore(dstkey, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#brpoplpush(java.lang.String,
	 * java.lang.String, int)
	 */
    @Override
    public String brpoplpush(String source, String destination, int timeout) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpoplpush(source, destination, timeout);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zunionstore(byte[],
	 * redis.clients.jedis.ZParams, byte)
	 */
    @Override
    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zunionstore(dstkey, params, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setbit(java.lang.String, long,
	 * boolean)
	 */
    @Override
    public Boolean setbit(String key, long offset, boolean value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setbit(key, offset, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zinterstore(byte[], byte)
	 */
    @Override
    public Long zinterstore(byte[] dstkey, byte[]... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zinterstore(dstkey, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getbit(java.lang.String, long)
	 */
    @Override
    public Boolean getbit(String key, long offset) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getbit(key, offset);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#zinterstore(byte[],
	 * redis.clients.jedis.ZParams, byte)
	 */
    @Override
    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zinterstore(dstkey, params, sets);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setrange(java.lang.String, long,
	 * java.lang.String)
	 */
    @Override
    public Long setrange(String key, long offset, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setrange(key, offset, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#save()
	 */
    @Override
    public String save() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.save();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getrange(java.lang.String, long,
	 * long)
	 */
    @Override
    public String getrange(String key, long startOffset, long endOffset) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getrange(key, startOffset, endOffset);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#bgsave()
	 */
    @Override
    public String bgsave() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.bgsave();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#configGet(java.lang.String)
	 */
    @Override
    public List<String> configGet(String pattern) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.configGet(pattern);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#bgrewriteaof()
	 */
    @Override
    public String bgrewriteaof() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.bgrewriteaof();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#configSet(java.lang.String,
	 * java.lang.String)
	 */
    @Override
    public String configSet(String parameter, String value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.configSet(parameter, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lastsave()
	 */
    @Override
    public Long lastsave() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lastsave();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#shutdown()
	 */
    @Override
    public String shutdown() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.shutdown();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#eval(java.lang.String, int,
	 * java.lang.String)
	 */
    @Override
    public Object eval(String script, int keyCount, String... params) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.eval(script, keyCount, params);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#info()
	 */
    @Override
    public String info() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.info();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slaveof(java.lang.String, int)
	 */
    @Override
    public String slaveof(String host, int port) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slaveof(host, port);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#eval(java.lang.String,
	 * java.util.List, java.util.List)
	 */
    @Override
    public Object eval(String script, List<String> keys, List<String> args) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.eval(script, keys, args);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slaveofNoOne()
	 */
    @Override
    public String slaveofNoOne() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slaveofNoOne();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#eval(java.lang.String)
	 */
    @Override
    public Object eval(String script) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.eval(script);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#configGet(byte[])
	 */
    @Override
    public List<byte[]> configGet(byte[] pattern) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.configGet(pattern);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#evalsha(java.lang.String)
	 */
    @Override
    public Object evalsha(String script) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.evalsha(script);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#configResetStat()
	 */
    @Override
    public String configResetStat() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.configResetStat();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#configSet(byte[], byte[])
	 */
    @Override
    public byte[] configSet(byte[] parameter, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.configSet(parameter, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#isConnected()
	 */
    @Override
    public boolean isConnected() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.isConnected();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#strlen(byte[])
	 */
    @Override
    public Long strlen(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.strlen(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#evalsha(java.lang.String,
	 * java.util.List, java.util.List)
	 */
    @Override
    public Object evalsha(String sha1, List<String> keys, List<String> args) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.evalsha(sha1, keys, args);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#lpushx(byte[], byte[])
	 */
    @Override
    public Long lpushx(byte[] key, byte[] string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpushx(key, string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#evalsha(java.lang.String, int,
	 * java.lang.String)
	 */
    @Override
    public Object evalsha(String sha1, int keyCount, String... params) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.evalsha(sha1, keyCount, params);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#persist(byte[])
	 */
    @Override
    public Long persist(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.persist(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#rpushx(byte[], byte[])
	 */
    @Override
    public Long rpushx(byte[] key, byte[] string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpushx(key, string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptExists(java.lang.String)
	 */
    @Override
    public Boolean scriptExists(String sha1) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptExists(sha1);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#echo(byte[])
	 */
    @Override
    public byte[] echo(byte[] string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.echo(string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptExists(java.lang.String)
	 */
    @Override
    public List<Boolean> scriptExists(String... sha1) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptExists(sha1);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#linsert(byte[],
	 * redis.clients.jedis.BinaryClient.LIST_POSITION, byte[], byte[])
	 */
    @Override
    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.linsert(key, where, pivot, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.yumo.core.cache.redis.IRedis#debug(redis.clients.jedis.DebugParams)
	 */
    @Override
    public String debug(DebugParams params) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.debug(params);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptLoad(java.lang.String)
	 */
    @Override
    public String scriptLoad(String script) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptLoad(script);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getClient()
	 */
    @Override
    public Client getClient() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getClient();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slowlogGet()
	 */
    @Override
    public List<Slowlog> slowlogGet() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slowlogGet();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#brpoplpush(byte[], byte[], int)
	 */
    @Override
    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpoplpush(source, destination, timeout);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slowlogGet(long)
	 */
    @Override
    public List<Slowlog> slowlogGet(long entries) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slowlogGet(entries);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#objectRefcount(java.lang.String)
	 */
    @Override
    public Long objectRefcount(String string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.objectRefcount(string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setbit(byte[], long, byte[])
	 */
    @Override
    public Boolean setbit(byte[] key, long offset, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setbit(key, offset, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#objectEncoding(java.lang.String)
	 */
    @Override
    public String objectEncoding(String string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.objectEncoding(string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getbit(byte[], long)
	 */
    @Override
    public Boolean getbit(byte[] key, long offset) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getbit(key, offset);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#objectIdletime(java.lang.String)
	 */
    @Override
    public Long objectIdletime(String string) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.objectIdletime(string);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#setrange(byte[], long, byte[])
	 */
    @Override
    public Long setrange(byte[] key, long offset, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setrange(key, offset, value);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getrange(byte[], long, long)
	 */
    @Override
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getrange(key, startOffset, endOffset);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#publish(byte[], byte[])
	 */
    @Override
    public Long publish(byte[] channel, byte[] message) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.publish(channel, message);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#psubscribe(redis.clients.jedis.
	 * BinaryJedisPubSub, byte)
	 */
    @Override
    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {

        Jedis jedis = null;
        try {
            jedis.psubscribe(jedisPubSub, patterns);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#getDB()
	 */
    @Override
    public Long getDB() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getDB();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#eval(byte[], java.util.List,
	 * java.util.List)
	 */
    @Override
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.eval(script, keys, args);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#eval(byte[], byte[], byte[][])
	 */
    @Override
    public Object eval(byte[] script, byte[] keyCount, byte[][] params) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.eval(script, keyCount, params);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptFlush()
	 */
    @Override
    public String scriptFlush() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptFlush();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptExists(byte)
	 */
    @Override
    public List<Long> scriptExists(byte[]... sha1) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptExists(sha1);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptLoad(byte[])
	 */
    @Override
    public byte[] scriptLoad(byte[] script) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptLoad(script);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#scriptKill()
	 */
    @Override
    public String scriptKill() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scriptKill();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slowlogReset()
	 */
    @Override
    public String slowlogReset() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slowlogReset();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slowlogLen()
	 */
    @Override
    public long slowlogLen() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slowlogLen();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slowlogGetBinary()
	 */
    @Override
    public List<byte[]> slowlogGetBinary() {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slowlogGetBinary();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#slowlogGetBinary(long)
	 */
    @Override
    public List<byte[]> slowlogGetBinary(long entries) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.slowlogGetBinary(entries);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#objectRefcount(byte[])
	 */
    @Override
    public Long objectRefcount(byte[] key) {

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.objectRefcount(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#objectEncoding(byte[])
	 */
    @Override
    public byte[] objectEncoding(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.objectEncoding(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * com.mobike.iotcloud.backend.framework.cache.PersistentCache#objectIdletime(byte[])
	 */
    @Override
    public Long objectIdletime(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.objectIdletime(key);
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
    }

    public Pool<Jedis> getPool() {
        return pool;
    }

    public void setPool(Pool<Jedis> pool) {
        this.pool = pool;
    }
}
