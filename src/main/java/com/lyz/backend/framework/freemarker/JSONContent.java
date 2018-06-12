package com.lyz.backend.framework.freemarker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * html处理类
 *
 * @author luyongzhao
 */
public class JSONContent {
    public static String json(Object o) {
        return json(o, true);
    }

    public static String json(Object o, Boolean format) {
        if (format) {
            return JSON.toJSONString(o, SerializerFeature.PrettyFormat,
                    SerializerFeature.DisableCircularReferenceDetect);
        } else {
            return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
        }
    }

    public static Object parse(String json) {
        return JSON.parse(json);
    }
}
