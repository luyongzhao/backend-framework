package com.mobike.iotcloud.backend.framework.interceptor;

import com.mobike.iotcloud.backend.framework.controller.bean.TicketBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 开放Api拦截器
 */
@Slf4j
public class OpenApiInterceptor implements HandlerInterceptor {

    private static final int expiredSeconds = 30;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        log.debug("open api interceptor is calling...");

        //进入的请求需要验证accountId和ticket
        String appId = httpServletRequest.getHeader(TicketBuilder.APP_ID_ATTR);

        /**
         if (StringUtils.isBlank(appId)) {
         log.warn("accountId is blank!");
         return false;
         }

         if (StringUtils.isBlank(ticket)) {
         log.warn("ticket is blank!");
         return false;
         }


        //TODO: 验证appId是否存在，是否合法

        //解析ticket
        AppUserAgent appUserAgent = TicketBuilder.getInstance(appId).parse(httpServletRequest);

        //解析失败则直接返回错误
        if (appUserAgent == null) {

            httpServletResponse.getWriter().write(JsonUtil.toJsonString(RestResponse.errorTicket));
            return false;
        }

        //应用（账户）id不一致，则直接返回错误
        if (StringUtils.equals(appId,appUserAgent.getAccountId())) {

            httpServletResponse.getWriter().write(JsonUtil.toJsonString(RestResponse.errorAppId));
            return false;
        }

        //随机数不能为空
        if (StringUtils.isBlank(appUserAgent.getRandomStr())) {

            httpServletResponse.getWriter().write(JsonUtil.toJsonString(RestResponse.error("random string can not be empty!")));
            return false;
        }

        //验证请求是否失效
        long now = System.currentTimeMillis();
        if ( now-appUserAgent.getTimestamp()>expiredSeconds ) {

            httpServletResponse.getWriter().write(JsonUtil.toJsonString(RestResponse.expiredRequest));
            return false;
        }


        //加入threadlocal，便于后续执行方法中获取上下文信息，特别是accountId
        ThreadLocalContext.put(AppUserAgent.class,appUserAgent);

         **/



        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        //出去的请求header中需要添加accountId和ticket
        log.debug("post handle is running...");

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
