package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

public class RSASignUtil {

    private static final Logger log = LoggerFactory.getLogger(RSASignUtil.class);


    private static final String defaultSignName = "sign";


    /**
     * 私钥签名
     * @param paras
     * @param privateKey
     * @return
     */
    public static String getSign(Map<String, Object> paras, String privateKey){

        return getSign(paras,defaultSignName,privateKey);
    }

    /**
     * 私钥签名
     * @param paras
     * @return
     */
    public static String getSign(Map<String, Object> paras, String signKeyNameInParas, String privateKey) {

        //预处理
        String strBeforeMd5 = preProcess(paras,signKeyNameInParas);
        //生成MD5
        String digestMD5 = getMD5(strBeforeMd5);

        //加密
        return RSAUtil.encryptWithPrivateKey(digestMD5,privateKey);
    }


    public static boolean isValidSign(Map<String, Object> paras, String publicKey){


        return isValidSign(paras,defaultSignName,publicKey);
    }


    /**
     * 公钥验签
     * @param paras 参数map
     * @param signKeyNameInParas 参数map中签名的key名称
     * @param publicKey
     * @return
     */
    public static boolean isValidSign(Map<String, Object> paras, String signKeyNameInParas, String publicKey){

        //解密数据
        String decryptedValue = RSAUtil.decryptWithPublicKey((String)paras.get(signKeyNameInParas),publicKey);

        //解密失败，直接返回
        if (decryptedValue == null) {

            return false;
        }

        //用现有参数生成MD5
        String strBeforeMd5 = preProcess(paras,signKeyNameInParas);
        String digestMD5 = getMD5(strBeforeMd5);

        //比较是否相等
        return decryptedValue.equals(digestMD5);
    }




    private static String getMD5(String str) {
        try{
            return DigestUtils.md5Hex(str.getBytes("UTF-8")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            log.error("fail to get MD5");
            throw new RuntimeException("fail to get MD5");
        }
    }

    private static String preProcess(Map<String, Object> paras,String signKeyNameInParas) {

        //各参数按字典排序 -> 按URL键值对拼接成字符串
        Map<String, String> sortMap = new TreeMap<String, String>();
        for (Map.Entry<String,Object> entry : paras.entrySet()) {
            String paramName = entry.getKey();
            String paramVal = entry.getValue().toString();

            //参数有值才加入map
            if (!signKeyNameInParas.equals(paramName.trim()) || paramVal != null || !"".equals(paramVal.trim())) {
                sortMap.put(paramName, paramVal);
            }
        }

        //按顺序拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }


        if (StringUtils.isBlank(builder.toString())) {
            return null;
        }

        //去除最后的'&'
        String str = builder.substring(0, builder.length() - 1);
        return str;
    }
}
