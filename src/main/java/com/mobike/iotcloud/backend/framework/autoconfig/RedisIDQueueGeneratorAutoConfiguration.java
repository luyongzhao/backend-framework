package com.mobike.iotcloud.backend.framework.autoconfig;


import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import com.mobike.iotcloud.backend.framework.id.IDGenerator;
import com.mobike.iotcloud.backend.framework.id.impl.RedisIDIncrementGeneratorImpl;
import com.mobike.iotcloud.backend.framework.id.impl.RedisIDQueueGeneratorImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RedisIDGeneratorProperties.QueueGenerator.class})
@Slf4j
@ConditionalOnProperty(name = "id.generator.queue.key")
public class RedisIDQueueGeneratorAutoConfiguration {


    @Autowired
    private RedisIDGeneratorProperties.QueueGenerator queueGenerator;

    @Autowired
    private PersistentCache persistentCache;


    @Bean("queueGenerator")
    public IDGenerator getRedisIDQueueGenerator() {

        String key = queueGenerator.getKey();

        if (StringUtils.isBlank(key)) {

            throw new RuntimeException("\"id.generator.queue.key\" should be added to application.properties!");
        }

        return new RedisIDQueueGeneratorImpl(queueGenerator.getKey(), queueGenerator.getCacheSize(), persistentCache).init();
    }

}
