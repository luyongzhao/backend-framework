package com.lyz.backend.framework.autoconfig;


import com.lyz.backend.framework.id.IDGenerator;
import com.lyz.backend.framework.cache.PersistentCache;
import com.lyz.backend.framework.id.impl.RedisIDIncrementGeneratorImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RedisIDGeneratorProperties.IncrGenerator.class,RedisIDGeneratorProperties.QueueGenerator.class})
@Slf4j
@ConditionalOnProperty(name = "id.generator.incr.key")
public class RedisIDIncreaseGeneratorAutoConfiguration {


    @Autowired
    private RedisIDGeneratorProperties.IncrGenerator incrGenerator;


    @Autowired
    private PersistentCache persistentCache;

    @Bean("increaseGenerator")
    public IDGenerator getRedisIDIncreatementGenerator() {

        String key = incrGenerator.getKey();

        if (StringUtils.isBlank(key)) {

            throw new RuntimeException("\"id.generator.incr.key\" should be added to application.properties!");
        }

        return new RedisIDIncrementGeneratorImpl(incrGenerator.getKey(), incrGenerator.getCacheSize(), incrGenerator.getInitValue(), persistentCache).init();
    }


}
