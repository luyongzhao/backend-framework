package com.mobike.iotcloud.backend.framework.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.mobike.iotcloud.backend.framework.autoconfig.InterceptorProperties;
import com.mobike.iotcloud.backend.framework.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableConfigurationProperties(InterceptorProperties.class)
@Slf4j
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private InterceptorProperties interceptorProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        if (interceptorProperties.getList()==null || interceptorProperties.getList().isEmpty()) {

            log.debug("no interceptor is configured!");
            return;
        }

        for (InterceptorProperties.Single single : interceptorProperties.getList()) {

            log.debug("=======init interceptor,className:{},include:{},exclude:{}",single.getClassName(),single.getInclude(),single.getExclude());

            if (StringUtils.isBlank(single.getClassName())) {

                throw new RuntimeException("className of intercetor can not be empty!");
            }

            if (StringUtils.isBlank(single.getInclude())) {

                throw new RuntimeException("include of intercetor can not be empty，include can be one or more,splited by \",\"!");
            }

            Object obj = ClassUtil.newInstance(single.getClassName(),null);

            if (obj == null) {
                throw new RuntimeException("fail to new instance :"+single.getClassName());
            }

            //不是HandlerInterceptor的子类，抛出异常
            if (!(obj instanceof HandlerInterceptor)) {

                throw new RuntimeException(single.getClassName()+" should implements org.springframework.web.servlet.HandlerInterceptor!");
            }

            //添加拦截器
            InterceptorRegistration interceptorRegistration = registry.addInterceptor((HandlerInterceptor) obj)
                    .addPathPatterns(single.getInclude().split(","));

            if (StringUtils.isNotBlank(single.getExclude())) {

                interceptorRegistration.excludePathPatterns(single.getExclude().split(","));
            }

        }


        super.addInterceptors(registry);
    }
}
