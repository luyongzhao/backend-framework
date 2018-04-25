package com.mobike.iotcloud.backend.framework.id;

import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
@Data
public abstract class AbstractRedisIDGenerator implements IDGenerator {

    protected Integer cacheSize = 20;
    protected String key;
    protected PersistentCache persistentCache;
    private String prefix;
    private SimpleDateFormat prefixDateFormat;

    public AbstractRedisIDGenerator init() {
        if (StringUtils.isNotBlank(prefix)) {
            if (prefix.equals("yyyyMMdd") || prefix.equals("yyyyMMddHHmm") || prefix.equals("yyyyMMddHHmmss")) {// 前缀
                prefixDateFormat = new SimpleDateFormat(prefix);
            }
        } else {
            prefix = null;
        }

        return this;
    }

    protected String prefix() {
        if (prefixDateFormat != null) {
            return prefixDateFormat.format(new Date());
        } else {
            return prefix;
        }
    }

}
