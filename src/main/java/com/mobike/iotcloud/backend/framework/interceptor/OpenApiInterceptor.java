package com.mobike.iotcloud.backend.framework.interceptor;

import com.mobike.iotcloud.backend.framework.controller.bean.AppUserAgent;
import com.mobike.iotcloud.backend.framework.controller.bean.RestResponse;
import com.mobike.iotcloud.backend.framework.controller.bean.SignValidator;
import com.mobike.iotcloud.backend.framework.controller.bean.TicketBuilder;
import com.mobike.iotcloud.backend.framework.util.JsonUtil;
import com.mobike.iotcloud.backend.framework.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 开放Api拦截器
 */
@Slf4j
public class OpenApiInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        log.debug("open api interceptor is calling...");


        /**

        final StringBuilder errMsg = new StringBuilder();

        //验证是否为合法的请求
        AppUserAgent appUserAgent = SignValidator.getInstance().validate(httpServletRequest,errMsg);

        if (appUserAgent == null) {

            httpServletResponse.getWriter().write(JsonUtil.toJsonString(RestResponse.error(errMsg.toString())));

            return false;
        }

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
