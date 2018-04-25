package com.mobike.iotcloud.backend.framework.autoconfig;

import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import com.mobike.iotcloud.backend.framework.cache.impl.RedisPersistentCacheImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
@EnableConfigurationProperties({PersistentCacheProperties.class,PersistentCacheProperties.PoolConfig.class})
@ConditionalOnClass(PersistentCache.class) //当类路径存在这个类时才会加载这个配置类，否则跳过,这个很有用比如不同jar包间类依赖，依赖的类不存在直接跳过，不会报错
@Slf4j
public class PersistentCacheAutoConfiguration {

    @Autowired
    private PersistentCacheProperties persistentCacheProperties;

    @Autowired
    private PersistentCacheProperties.PoolConfig poolConfig;

    @Bean("persistentCache")
    public PersistentCache getPersistentCache(){

        //TODO:目前只有redis,不按照type做条件判断

        if (!"redis".equals(persistentCacheProperties.getType())) {

            throw new RuntimeException("\"persistent.cache.type=redis\" should be added to application.properties!");
        }

        if (StringUtils.isBlank(persistentCacheProperties.getHost())) {

            throw new RuntimeException("\"persistent.cache.host=${host}\" should be added to application.properties!");
        }

        if (persistentCacheProperties.getPort() == null) {

            throw new RuntimeException("\"persistent.cache.prot=${port}\" should be added to application.properties!");
        }

        PersistentCache persistentCache = null;

        JedisPool pool = null;

        if (StringUtils.isBlank(persistentCacheProperties.getPassword())){

            pool = new JedisPool(poolConfig,persistentCacheProperties.getHost(),persistentCacheProperties.getPort(),persistentCacheProperties.getTimeout());
        }else {

            pool = new JedisPool(poolConfig,persistentCacheProperties.getHost(),persistentCacheProperties.getPort(),persistentCacheProperties.getTimeout(),persistentCacheProperties.getPassword());
        }

        persistentCache = new RedisPersistentCacheImpl(pool);

        log.debug("redis config:minIdle={}",poolConfig.getMinIdle());

        return persistentCache;

    }

}
