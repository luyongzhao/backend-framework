package com.mobike.iotcloud.backend.framework.controller.bean;

import com.alibaba.fastjson.JSON;
import com.mobike.iotcloud.backend.framework.util.AES128Factory;
import com.mobike.iotcloud.backend.framework.util.Configs;
import com.mobike.iotcloud.backend.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class MobileTicketBuilder {


//	private static AES128Factory aes128Factory = new AES128Factory(Configs.get("aesKeyForrestAuthHeader"));

//	public static final String HTTP_TICKET_NAME = "ticket";

    private static final Map<String,MobileTicketBuilder> appID2MobileTicketBuilder = new HashMap<String, MobileTicketBuilder>();

    private AES128Factory aes128Factory = null;

    private static final String[] appIDs = {"default"};

    private static final Set<String> appIDSet = new HashSet<>();

    private String appID = null;

    static{

        for(String appID : appIDs)
        {
            appIDSet.add(appID);
            appID2MobileTicketBuilder.put(appID, new MobileTicketBuilder(appID));
        }
    }


    private MobileTicketBuilder()
    {

    }

    private MobileTicketBuilder(String appID)
    {
        this.appID = appID;
        aes128Factory = new AES128Factory(Configs.get("aesKeyForrestAuthHeader_"+appID));
    }

    public static synchronized MobileTicketBuilder getInstance(String appID)
    {

        MobileTicketBuilder builder = appID2MobileTicketBuilder.get(appID);
        if(builder == null)
        {
            return null;
        }

        return builder;
    }

    public static MobileTicketBuilder getInstance()
    {
        return getInstance("default");
    }

    public static boolean isValidAppID(String appID)
    {
        return appIDSet.contains(appID);
    }

    public String build(MobileUserAgent agent)
    {
        String json = JsonUtil.toJSONString(agent);
        String ticket = aes128Factory.encrypt(json);
        try
        {
            return URLEncoder.encode(ticket, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            return ticket;
        }
    }

    private MobileUserAgent decode(String ticket)
    {
        log.info("no url decode TICKET: " + ticket);
        try
        {
            ticket = URLDecoder.decode(ticket, "UTF-8");

            //去除空格
//			ticket = ticket.replace(" ", "");

            String decodeTicket = aes128Factory.decrypt(ticket);
            log.info("TICKET: " + ticket);
            log.info("decode TICKET: " + decodeTicket);
            if (StringUtils.isNotBlank(decodeTicket))
            {
                return JsonUtil.parseJSONObject(decodeTicket.trim(), MobileUserAgent.class);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return null;
    }

    public String encode(String ticket)
    {

        String code;
        try
        {
            String encodeStr = aes128Factory.encrypt(ticket);
            code = URLEncoder.encode(encodeStr,"UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return code;

    }




    public String decodeID(String encodeID)
    {
        try
        {
            String decodeID = aes128Factory.decrypt(encodeID);
            log.info("decode ID: " + decodeID);
            if (StringUtils.isNotBlank(decodeID)
                    && StringUtils.endsWith(decodeID, "@" + Configs.get("aesKeyForrestAuthHeader_"+this.appID)))
            {
                return decodeID.substring(0, decodeID.indexOf('@'));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return null;
    }
    /**
     * 账户编码
     * @param id
     * @return
     */
    public String encodeID(String id)
    {
        return aes128Factory.encrypt(id+"@"+Configs.get("aesKeyForrestAuthHeader_"+this.appID));
    }
    /**
     * 加密带有时效性的ID，ID中不能带有@号
     * @param id
     * @return 失败返回null
     */
    public String encodeExpiredID(String id)
    {
        if(StringUtils.contains(id, "@"))
        {
            log.error("id must not contains @");
            return null;
        }
        return aes128Factory.encrypt(id+"@"+Configs.get("aesKeyForrestAuthHeader_"+this.appID)+"@"+System.currentTimeMillis());
    }
    /**
     *
     * @param encodeID
     * @param timeoutSeconds
     * @return 成功返回null，失败返回错误码，400-错误的加密码，500-超时
     */
    public String checkExpiredID(String encodeID,long timeoutSeconds,String originID)
    {
        try
        {
            String decodeID = aes128Factory.decrypt(encodeID);
            if(decodeID == null)
            {
                log.error("fail to parse encodeID:"+encodeID);
                return "400";
            }
            log.info("decode ID: " + decodeID);
            String[] partsOfDecodeID = decodeID.split("@");

            //错误的加密码
            if(partsOfDecodeID.length!=3)
            {
                log.warn("error format of decodeID:"+decodeID);
                return "400";
            }
            if(!StringUtils.equals(Configs.get("aesKeyForrestAuthHeader_"+this.appID), partsOfDecodeID[1]))
            {
                log.warn("aesKey is wrong:"+partsOfDecodeID[1]);
                return "400";
            }
            if(!StringUtils.equals(originID, partsOfDecodeID[0]))
            {
                log.warn("originID not equals to id in encodeID:"+originID);
                return "400";
            }
            //判断是否超时
            long time = NumberUtils.toLong(partsOfDecodeID[2],0);
            long now = System.currentTimeMillis();
            if((now-time)>timeoutSeconds*1000)
            {
                return "500";
            }
        } catch (Exception e)
        {
            return "400";
        }

        return null;
    }


    public static final String REQ_TICKET_NAME = "ticket";

    public MobileUserAgent parse(HttpServletRequest req)
    {
        int requestType = MobileUserAgent.RequestType.unkown;

        String ticket = req.getHeader(REQ_TICKET_NAME);
        if (StringUtils.isNotBlank(ticket))
        {// 请求头中的肯定是app的请求
            requestType = MobileUserAgent.RequestType.rest;
        } else
        {
            // 好吧， 放弃了，还是用session吧， fix各种蛋疼的微信浏览器
            HttpSession session = req.getSession(false);
            if (session != null)
            {// 仅h5页使用
                ticket = (String) session.getAttribute(REQ_TICKET_NAME);
            }

            if (StringUtils.isNotBlank(ticket))
            {// h5
                requestType = MobileUserAgent.RequestType.web;
            } else
            {
                requestType = MobileUserAgent.RequestType.unkown;
            }
        }

        MobileUserAgent agent = null;
        if (StringUtils.isNotBlank(ticket))
        {
            agent = decode(ticket);
            if(agent == null)
            {
                return null;
            }
        }

        if (agent == null)
        {
            agent = new MobileUserAgent();
        }

        agent.setIp(req.getRemoteHost());
        agent.setUri(req.getRequestURI());
        agent.setRequestType(requestType);
        agent.setQueryParam(req.getQueryString());

        return agent;
    }
}
