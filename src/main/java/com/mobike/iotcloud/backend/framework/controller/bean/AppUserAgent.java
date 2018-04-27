package com.mobike.iotcloud.backend.framework.controller.bean;

import com.mobike.iotcloud.backend.framework.util.ThreadLocalContext;
import lombok.Data;

@Data
public class AppUserAgent {

    //账户id
    private String accountId;

    //产品Id
    private String productId;

    //请求时间戳
    private long timestamp;

    //签名
    private String sign;


    public static AppUserAgent current()
    {
        return ThreadLocalContext.get(AppUserAgent.class);
    }


    /**
     * 用户需要实现该接口，自定义header中字段名称
     */
    public static interface FieldName{


        String getAccountIdName();

        String getProductIdName();

        String getTimestampName();

        String getSignName();

        /**
         * 所有名称开头公共的字符串，
         * @return
         */
        String getNameStartWith();
    }


}
