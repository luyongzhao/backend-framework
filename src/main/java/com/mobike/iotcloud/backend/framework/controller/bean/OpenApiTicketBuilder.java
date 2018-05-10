package com.mobike.iotcloud.backend.framework.controller.bean;

import com.alibaba.fastjson.JSON;
import com.mobike.iotcloud.backend.framework.util.AES128Factory;
import com.mobike.iotcloud.backend.framework.util.JsonUtil;
import com.mobike.iotcloud.backend.framework.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rest请求票据拼接
 *
 * @author luyongzhao
 */
@Slf4j
@Deprecated
public class OpenApiTicketBuilder {

    private static final Map<String, OpenApiTicketBuilder> appID2TicketBuilder = new ConcurrentHashMap<String,OpenApiTicketBuilder>();

    private AES128Factory aes128Factory = null;

    //应用id，对于摩拜来讲就是账户id
    private String appID = null;

    public static final String ATTR_APP_ID = "MOBIKE-ACCOUNT-ID";

    public static final String ATTR_TICKET = "MOBIKE-TICKET";

    public static final String ATTR_TIMESTAMP = "MOBIKE-TIMESTAMP";

    private static final int expiredSeconds = 30;


    private OpenApiTicketBuilder() {

    }

    //TODO:初始化，后续需要从数据库或者下层接口获取
    static{
        OpenApiTicketBuilder mobikeBuilder = new OpenApiTicketBuilder("mobike");
        appID2TicketBuilder.put("mobike",mobikeBuilder);
    }

    private OpenApiTicketBuilder(String appID) {

        this.appID = appID;
        //TODO:此处需要获取应用的密钥密钥必须要16个字符
        aes128Factory = new AES128Factory("adkjffdkajkjkjnm");
    }

    public static synchronized OpenApiTicketBuilder getInstance(String appID) {

        OpenApiTicketBuilder builder = appID2TicketBuilder.get(appID);
        if (builder == null) {
            return null;
        }

        return builder;
    }



    public String build(AppUserAgent agent) {
        String json = JSON.toJSONString(agent);
        String ticket = aes128Factory.encrypt(json);
        try {
            return URLEncoder.encode(ticket, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return ticket;
        }
    }

    private AppUserAgent decode(String ticket) {
        log.debug("no url decode TICKET: " + ticket);
        try {
            ticket = URLDecoder.decode(ticket, "UTF-8");

            String decodeTicket = aes128Factory.decrypt(ticket);
            log.debug("TICKET: {}" , ticket);
            log.debug("decode TICKET: {}" , decodeTicket);
            if (StringUtils.isNotBlank(decodeTicket)) {
                return JsonUtil.parseJSONObject(decodeTicket.trim(), AppUserAgent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    public String encode(String ticket) {

        String code;
        try {
            String encodeStr = aes128Factory.encrypt(ticket);
            code = URLEncoder.encode(encodeStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return code;

    }


    /**
     *
     * @param req
     * @param errorMsg 输出参数：解析过程中出现的错误信息
     * @return
     */
    public AppUserAgent parse(HttpServletRequest req, final StringBuilder errorMsg) {

        //进入的请求需要验证accountId和ticket
        String appId = req.getHeader(OpenApiTicketBuilder.ATTR_APP_ID);
        String ticket = req.getHeader(OpenApiTicketBuilder.ATTR_TICKET);


        String err = null;

        if (StringUtils.isBlank(appId)) {
            err = "accountId is blank in header!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        if (StringUtils.isBlank(ticket)) {
            err = "ticket is blank in header!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        //TODO: 验证appId是否存在，是否合法

        AppUserAgent appUserAgent = decode(ticket);
        //解析失败则直接返回错误
        if (appUserAgent == null) {

            err = "fail to parse ticket!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        //应用（账户）id不一致，则直接返回错误
        if (StringUtils.equals(appId,appUserAgent.getAccountId())) {

            err = "appId in header not equals to the one in ticket!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        //随机数不能为空
        /**
        if (StringUtils.isBlank(appUserAgent.getRandomStr())) {

            err = "random string can not be empty!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }*/

        //验证请求是否失效
        long now = System.currentTimeMillis();
        if ( (now-appUserAgent.getTimestamp())/1000>expiredSeconds ) {

            err = "request is expired!";
            log.warn(err);
            errorMsg.append(err);
            return null;
        }

        //加入threadlocal，便于后续执行方法中获取上下文信息，特别是accountId
        ThreadLocalContext.put(AppUserAgent.class,appUserAgent);

        return appUserAgent;
    }

    public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 20; i++)
//		{
//			String code = "sdf966ea1d20aa1d1d7fc50cb59f951dc410be07f8ab9fbca672c0fb&cn& & & & &320&568&ios&2743C965-F011-45E2-8CF5-66EB8C86D50D& ";
//			AES128 aes128 = new AES128("2)&dh3JlEsf323p9");
//			String newcode = aes128.encryptWithBase64(code);
//			newcode = URLEncoder.encode(newcode, "UTF-8");
//			newcode = "7mV%2FPXusO%2BDLI1vatF42fasw1lyzh%2Fs8aHy6vBgnTT86xm7%2BQ8KYAlfqU1N%2BT3S0ekLm%2BKnuu7w%2B6%2FcYpJ4v3ThAuXUhJbGhOFcSaUVG0wrFq05P5e42TusXt%2BZ%2B28CPP9yDDMu3SfQpmX3%2BY8qkqoJqoHHbXR6Au1cUoVjS8uefxMPP6x2sByJSOBS7p1MM";
//			newcode = URLDecoder.decode(newcode, "UTF-8");
//			newcode = aes128Factory.decrypt(newcode);
//			System.out.println(newcode);
//		}
        String data = "{\"os\":2,\"version\":\"1.0\",\"deviceToken\":\"\",\"screenWidth\":640,\"screenHeight\":960,\"uuid\":1111,\"device\":\"ipad\",\"pkgID\":\"xschool\",\"lang\":\"ch\",\"userID\":\"1\",\"ip\":\"192.168.1.1\",\"uri\":\"http://m.xschool.com/tp/student/sendRegSms\",\"requestType\":1}";
        String encodeData = OpenApiTicketBuilder.getInstance("mobike").encode(data);
        System.out.println(encodeData);
        System.out.println(OpenApiTicketBuilder.getInstance("mobike").decode(encodeData));
    }
}
