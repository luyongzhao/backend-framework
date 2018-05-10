package com.mobike.iotcloud.backend.framework.controller.bean;

import com.mobike.iotcloud.backend.framework.auth.Signer;
import com.mobike.iotcloud.backend.framework.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名验证类
 */
@Slf4j
public class SignValidator {

    private static final String decode = "utf-8";


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
     *
     *
     * @param req
     * @param fieldName header中参数名称，需要实现AppUserAgent.FieldName，自定义header中字段名称
     * @return 不合法请求返回null
     */
    public AppUserAgent validate(HttpServletRequest req, AppUserAgent.FieldName fieldName, String accountSecret) throws Exception{


        //进入的请求需要验证accountId和ticket
        String productId = req.getHeader(fieldName.getProductIdName());
        String sign = req.getHeader(fieldName.getSignName());
        String accountId = req.getHeader(fieldName.getAccountIdName());
        String timestamp = req.getHeader(fieldName.getTimestampName());


        String err = null;

        if (StringUtils.isBlank(accountId)) {

            err = fieldName.getAccountIdName() + " is blank in header!";
            throw new Exception(err);
        }

        if (StringUtils.isBlank(productId)) {

            err = fieldName.getProductIdName() + " is blank in header!";
            throw new Exception(err);
        }

        if (StringUtils.isBlank(sign)) {

            err = fieldName.getSignName() + " is blank in header!";
            throw new Exception(err);
        }

        if (StringUtils.isBlank(timestamp)) {

            err = fieldName.getTimestampName() + " is blank in header!";
            throw new Exception(err);
        }

        long timestampL = NumberUtils.toLong(timestamp, -1);
        if (timestampL == -1) {

            err = fieldName.getTimestampName() + " is not a valid number!";
            throw new Exception(err);
        }

        //验证请求是否失效
        long now = System.currentTimeMillis();
        if ((now - timestampL) / 1000 > expiredSeconds) {

            err = "request is expired!";
            throw new Exception(err);
        }

        //对数据进行签名，验证是否跟用户上传的一致
        boolean flag = validateSign(req, fieldName, sign, accountSecret);
        if (!flag) {
            err = fieldName.getSignName() + " is invalid!";
            throw new Exception(err);
        }

        AppUserAgent appUserAgent = new AppUserAgent();
        appUserAgent.setAccountId(accountId);
        appUserAgent.setTimestamp(timestampL);
        appUserAgent.setProductId(productId);


        return appUserAgent;

    }

    private boolean validateSign(HttpServletRequest req, AppUserAgent.FieldName fieldName, String sign, String accountSecret) {

        //对参数排序
        Map<String, String> sortMap = new TreeMap<>();

        //获取header中的数据
        Enumeration<String> enumeraton = req.getHeaderNames();
        while (enumeraton.hasMoreElements()) {

            String headerName = enumeraton.nextElement();


            if (headerName.startsWith(fieldName.getNameStartWith())) {

                sortMap.put(headerName, req.getHeader(headerName));
            }
        }

        //获取get请求中的参数(跟网上说法不一致，获取不到post中的参数)
        Map<String,String[]> paramMap = req.getParameterMap();

        for(Map.Entry<String,String[]> entry : paramMap.entrySet()){

            if (entry.getValue()==null ||entry.getValue().length==0) {
                continue;
            }

            String key = entry.getKey();
            for (String val : entry.getValue()) {

                //空数据不参与签名
                if (StringUtils.isBlank(val)) {
                    continue;
                }

                sortMap.put(key,val);
            }
        }

        //获取post方式url中的参数,TODO:很奇怪，post方式的获取不到查询字符串
        req.getRequestURL();
        String queryString = req.getQueryString();
        log.debug("queryString:{}",queryString);
        if (StringUtils.isNotBlank(queryString)) {

            String[] params = queryString.split("&");
            for (String param : params) {

                if (param.indexOf('=') == -1) {
                    continue;
                }
                String[] paramPair = param.split("=");
                //去除不成对的和值为空的数据
                if (paramPair.length <= 1 || StringUtils.isBlank(paramPair[1])) {
                    continue;
                }

                try {
                    sortMap.put(paramPair[0], URLDecoder.decode(paramPair[1],decode));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }


        //删除签名关键字
        sortMap.remove(fieldName.getSignName());

        //按顺序拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {

            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        //去除最后的&
        String strToSign = builder.substring(0, builder.length() - 1);
        log.debug("strToSign:{}",strToSign);

        //签名
        String newSign = Signer.getSigner(Signer.HMAC_SHA1).signString(strToSign, accountSecret);
        log.debug("compare sign,old sign:{},new sign:{}",sign,newSign);

        return newSign.equals(sign);

    }


}
