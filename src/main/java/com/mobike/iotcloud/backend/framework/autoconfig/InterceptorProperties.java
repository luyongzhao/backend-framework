package com.mobike.iotcloud.backend.framework.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器配置
 */

@Data
@ConfigurationProperties(prefix = "interceptor")
public class InterceptorProperties {


    private List<Single> list = new ArrayList<>();


    @Data
    public static class Single{

        /**拦截器类名称*/
        private String className;

         /**拦截包含表达式 */
        private String include;

        /**拦截不包含表达式*/
        private String exclude;

    }
}
