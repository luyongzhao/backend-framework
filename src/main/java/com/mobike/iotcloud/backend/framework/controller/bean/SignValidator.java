package com.mobike.iotcloud.backend.framework.controller.bean;

import com.mobike.iotcloud.backend.framework.auth.Signer;
import com.mobike.iotcloud.backend.framework.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名验证类
 */
@Slf4j
public class SignValidator {

    //应用id，对于摩拜来讲就是账户id
    private String appID = null;

    public static final String ATTR_APP_ID = "MOBIKE-ACCOUNT-ID";

    public static final String ATTR_TICKET = "MOBIKE-TICKET";

    public static final String ATTR_TIMESTAMP = "MOBIKE-TIMESTAMP";

    public static final String HEAD_ATTR = "MOBIKE-";

    public static final String secret = "adkjffdkajkjkjnm";


    private static final int expiredSeconds = 30;

    public static SignValidator signValidator = null;

    private SignValidator() {


    }

    public static synchronized SignValidator getInstance() {

        if (signValidator == null) {

            signValidator = new SignValidator();
        }

        return signValidator;
    }


    /**
     * 验证是否为合法的请求
     *
     * @param req
     * @param errorMsg 输出参数：解析过程中出现的错误信息
     * @return 不合法请求返回null
     */
    public AppUserAgent validate(HttpServletRequest req, final StringBuilder errorMsg) {


        //进入的请求需要验证accountId和ticket
        String appId = req.getHeader(ATTR_APP_ID);
        String sign = req.getHeader(ATTR_TICKET);
        String timestamp = req.getHeader(ATTR_TIMESTAMP);


        String err = null;

        if (StringUtils.isBlank(appId)) {

            err = ATTR_APP_ID + " is blank in header!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        if (StringUtils.isBlank(sign)) {

            err = ATTR_TICKET + " is blank in header!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        if (StringUtils.isBlank(timestamp)) {

            err = ATTR_TIMESTAMP + " is blank in header!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        long timestampL = NumberUtils.toLong(timestamp, -1);
        if (timestampL == -1) {

            err = ATTR_TIMESTAMP + " is not a valid number!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        //验证请求是否失效
        long now = System.currentTimeMillis();
        if ((now - timestampL) / 1000 > expiredSeconds) {

            err = "request is expired!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        //对数据进行签名，验证是否跟用户上传的一致
        boolean flag = validateSign(req, sign);
        if (!flag) {
            err = ATTR_TICKET + " is invalid!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        AppUserAgent appUserAgent = new AppUserAgent();
        appUserAgent.setAccountId(appId);
        appUserAgent.setTimestamp(timestampL);

        //加入threadlocal，便于后续执行方法中获取上下文信息，特别是accountId
        ThreadLocalContext.put(AppUserAgent.class, appUserAgent);

        return appUserAgent;

    }

    private boolean validateSign(HttpServletRequest req, String sign) {

        //对参数排序
        Map<String, String> sortMap = new TreeMap<>();

        //获取header中的数据
        Enumeration<String> enumeraton = req.getHeaderNames();
        while (enumeraton.hasMoreElements()) {

            String headerName = enumeraton.nextElement();
            if (headerName.startsWith(HEAD_ATTR)) {
                sortMap.put(headerName, req.getHeader(headerName));
            }
        }

        //获取请求中的参数
        enumeraton = req.getParameterNames();
        while (enumeraton.hasMoreElements()) {

            String paramName = enumeraton.nextElement();
            String paramVal = req.getParameter(paramName);

            //参数有值才加入map
            if (StringUtils.isNotBlank(paramVal)) {

                sortMap.put(paramName, paramVal);
            }
        }

        //按顺序拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {

            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        //去除最后的&
        String strToSign = builder.substring(0, builder.length() - 1);

        //签名
        String newSign = Signer.getSigner(Signer.HMAC_SHA1).signString(strToSign, secret);

        return newSign.equals(sign);

    }


}
