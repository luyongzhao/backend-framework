package weixin.popular.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class JsonUtil {

    public static String toJSONString(Object object){

        return JSON.toJSONString(object);
    }

    public static <T> T parseJSONObject(String jsonString, Class<T> clazz){

        return JSON.parseObject(jsonString,clazz);
    }

    public static Map objectToMap(Object object){

        JSONObject jsonObject = JSON.parseObject(toJSONString(object));

        return jsonObject;
    }


    public static Map jsonStringToMap(String jsonString){

        JSONObject jsonObject = JSON.parseObject(jsonString);

        return jsonObject;
    }
}
