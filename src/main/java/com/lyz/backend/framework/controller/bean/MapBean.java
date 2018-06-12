package com.lyz.backend.framework.controller.bean;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.HashMap;

public class MapBean extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public MapBean() {//
    }

    public MapBean(Object... objects) {
        if (objects != null && objects.length % 2 == 0) {
            for (int i = 0; i < objects.length; i += 2) {
                Object value = objects[i + 1];
                if (value != null) {// 手动剔除空值，jackson貌似对map没办法
                    this.put((String) objects[i], value);
                }
            }
        } else {
            throw new RuntimeException("错误的map参数长度");
        }
    }

    public Integer getInt(String key) {
        return ((Number) get(key)).intValue();
    }

    public Long getLong(String key) {
        return ((Number) get(key)).longValue();
    }

    public Double getDouble(String key) {
        return ((Number) get(key)).doubleValue();
    }

    public Boolean getBoolean(String key) {
        Object v = get(key);
        if (v == null) {
            return false;
        }

        if (v instanceof Boolean) {
            return (Boolean) v;
        } else {
            return Boolean.valueOf((String) v);
        }
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public Date getDate(String key) {
        return new Date(getLong(key));
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
