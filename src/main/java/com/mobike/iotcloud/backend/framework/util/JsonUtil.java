package com.mobike.iotcloud.backend.framework.util;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

    public static String toJsonString(Object object){

        return JSON.toJSONString(object);
    }

    public static <T> T parseJSONObject(String jsonString, Class<T> clazz){

        return JSON.parseObject(jsonString,clazz);
    }
}
