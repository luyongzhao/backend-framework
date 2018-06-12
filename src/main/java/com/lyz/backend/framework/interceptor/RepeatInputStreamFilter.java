package com.lyz.backend.framework.interceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by luyongzhao
 * 可重复获取输入流过滤器,需要在Application上增加注解@ServletComponentScan
 */
//@WebFilter(filterName="repeatInputstreamFilter",urlPatterns="/*")
public class RepeatInputStreamFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("--------------过滤器初始化------------");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("--------------执行过滤操作------------"+servletRequest.getClass());


        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);

        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("--------------过滤器销毁------------");
    }
}