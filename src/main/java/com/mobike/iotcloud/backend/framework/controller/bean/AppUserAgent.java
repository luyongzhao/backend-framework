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

    //随机字符串
    private String randomStr;


    public static AppUserAgent current()
    {
        return ThreadLocalContext.get(AppUserAgent.class);
    }


}
