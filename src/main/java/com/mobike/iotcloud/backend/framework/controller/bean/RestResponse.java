package com.mobike.iotcloud.backend.framework.controller.bean;

import com.mobike.iotcloud.backend.framework.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * ajax响应对象
 *
 * @author luyongzhao
 */
public class RestResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(RestResponse.class);

    public static final RestResponse success = new RestResponse();

    public static final RestResponse todo = RestResponse.error(RESULT_CODE.errorRequest, "unimplemented interface!");

    public static final RestResponse errorRequest = RestResponse.error("error param!");

    public static final RestResponse systemException = RestResponse.error(RESULT_CODE.systemException,
            "internal error!");

    //应用（账户）不存在或者不正确
    public static final RestResponse errorAppId = RestResponse.error(RESULT_CODE.errorAppId, "account Id error or does not exist!");

    //解析ticket失败
    public static final RestResponse errorTicket = RestResponse.error(RESULT_CODE.errorTicket, "fail to parse ticket!");

    //失效的请求
    public static final RestResponse expiredRequest = RestResponse.error(RESULT_CODE.expiredRequest, "expired request!");


    /**
     * 成功
     */
    private Integer code;
    private String desc;
    private Object data;


    public static class RESULT_CODE {

        public static final int success = 0;// 成功
        public static final int fail = 1;//默认失败

        public static final int errorRequest = 400; // 错误的请求参数
        public static final int expiredRequest = 401; // 失效的请求
        public static final int errorAppId = 402; // 账户id不正确
        public static final int errorTicket = 403; // ticket解析失败


        public static final int systemException = 500;// 系统错误


    }

    public RestResponse() {
        this.code = RESULT_CODE.success;
    }

    public static RestResponse error(String desc) {
        RestResponse rest = new RestResponse();
        rest.code = RESULT_CODE.fail;
        rest.desc = desc;

        log(rest);

        return rest;
    }


    public static RestResponse error(int code, String desc) {
        RestResponse rest = new RestResponse();
        rest.code = code;
        rest.desc = desc;
        log(rest);
        return rest;
    }

    public static RestResponse succ(Object data) {
        RestResponse rest = new RestResponse();
        rest.data = data;

        log(rest);

        return rest;
    }

    public static RestResponse succ(String desc, Object data) {
        RestResponse rest = new RestResponse();
        rest.data = data;
        rest.desc = desc;

        log(rest);

        return rest;
    }

    private static void log(RestResponse rest) {
        if (log.isDebugEnabled()) {
            AppUserAgent agent = AppUserAgent.current();
            if (agent != null) {
                log.debug(JsonUtil.toJSONString(new MapBean("apiUserAgent", agent, "restResponse", rest)));
            } else {
                log.debug(JsonUtil.toJSONString(rest));
            }
        }
    }

    public static RestResponse error(String desc, Integer code, Object data) {
        RestResponse rest = new RestResponse();
        rest.data = data;
        rest.desc = desc;
        rest.code = code;

        log(rest);

        return rest;
    }

    public boolean success() {

        return code == RESULT_CODE.success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
