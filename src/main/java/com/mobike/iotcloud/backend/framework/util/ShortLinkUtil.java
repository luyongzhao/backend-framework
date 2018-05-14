package com.mobike.iotcloud.backend.framework.util;


import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 新浪短连接服务
 */
public class ShortLinkUtil {


    public static final  String SOURCE = "475062332";

    public static final  String URLPRE = "http://api.t.sina.com.cn/short_url/shorten.json?";


    public static String toShortUrl(String longUrl)  {

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {
            httpclient = HttpClients.createDefault();

            // 创建httpget.
            HttpGet httpget = new HttpGet(URLPRE+"source="+SOURCE+"&url_long="+longUrl);

            // 执行get请求.
            response = httpclient.execute(httpget);

            // 获取响应实体
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() ==200 && entity != null) {

                return convertJson(EntityUtils.toString(entity));
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            try {
                httpclient.close();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private static String convertJson(String json){

        Object obj = JSON.parseObject(json.substring(1, json.length()-1).replaceAll("_",""),Result.class);

        Result uRrsult = (Result)obj;

        return uRrsult.getUrlshort();
    }


    public static void main(String[] args) {

        String url = "https://blog.csdn.net/u013473691/article/details/52297195";

        String urlShort = toShortUrl(url);

        System.out.println("**********短链接" + urlShort);

    }



    public static class Result{

        private String urlshort;
        private String urllong;
        private int type;

        public String getUrlshort(){
            return urlshort;
        }

        public String getUrllong(){
            return urllong;
        }

        public int getType(){
            return type;
        }

        public void setUrlshort(String urlshort){
            this.urlshort = urlshort;
        }

        public void setUrllong(String urllong){
            this.urllong = urllong;
        }

        public void setType(int type){
            this.type = type;
        }
    }



}
