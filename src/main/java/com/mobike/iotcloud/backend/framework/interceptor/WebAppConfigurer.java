package com.mobike.iotcloud.backend.framework.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        //添加拦截器
        registry.addInterceptor(new OpenApiInterceptor()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }
}
