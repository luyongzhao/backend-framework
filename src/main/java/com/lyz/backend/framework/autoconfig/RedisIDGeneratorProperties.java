package com.lyz.backend.framework.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class RedisIDGeneratorProperties {

    //redis的key值
    private String key;

    //本地缓存数量大小,批量从redis中获取的id数量
    private Integer cacheSize = 20;



    @Data
    @ConfigurationProperties(prefix = "id.generator.incr")
    public static class IncrGenerator extends RedisIDGeneratorProperties{

        //初始值
        protected Long initValue = 10000L;

    }

    @ConfigurationProperties(prefix = "id.generator.queue")
    public static class QueueGenerator extends RedisIDGeneratorProperties{



    }




}
