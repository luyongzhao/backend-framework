package com.mobike.iotcloud.backend.framework.id;

import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractRedisIDGenerator implements IDGenerator {
    protected Integer cacheSize = 20;
    protected String key;
    protected PersistentCache persistentCache;
    private String prefix;

    public void init() {
        if (StringUtils.isNotBlank(prefix)) {
            if (prefix.equals("yyyyMMdd") || prefix.equals("yyyyMMddHHmm") || prefix.equals("yyyyMMddHHmmss")) {// 前缀
                prefixDateFormat = new SimpleDateFormat(prefix);
            }
        } else {
            prefix = null;
        }
    }

    protected String prefix() {
        if (prefixDateFormat != null) {
            return prefixDateFormat.format(new Date());
        } else {
            return prefix;
        }
    }

    public Integer getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(Integer cacheSize) {
        this.cacheSize = cacheSize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PersistentCache getRedis() {
        return persistentCache;
    }

    public void setRedis(PersistentCache persistentCache) {
        this.persistentCache = persistentCache;
    }

    private SimpleDateFormat prefixDateFormat;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
