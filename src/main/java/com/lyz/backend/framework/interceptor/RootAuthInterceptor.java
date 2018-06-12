package com.lyz.backend.framework.interceptor;

import com.lyz.backend.framework.controller.bean.UserAgent;
import com.lyz.backend.framework.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component("rootAuthInterceptor")
public class RootAuthInterceptor implements HandlerInterceptor{

    private static String PROCESS_START_TIME = "_gProcessStart";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ThreadLocalContext.put(PROCESS_START_TIME, System.currentTimeMillis());
        request.setAttribute("_timer_", System.currentTimeMillis());

        UserAgent userAgent = new UserAgent(request, response);
        request.setAttribute("_userAgent_", userAgent);
        ThreadLocalContext.put(userAgent);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        log.debug("clean data in thread local...");
        ThreadLocalContext.clean();
    }
}
