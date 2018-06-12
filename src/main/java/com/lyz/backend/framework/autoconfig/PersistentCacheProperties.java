package com.lyz.backend.framework.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.JedisPoolConfig;

@Data
@ConfigurationProperties(prefix = "persistent.cache")
public class PersistentCacheProperties {


    private String type = "redis";

    private String host;

    private Integer port;

    private Integer timeout = 5000;

    private String password;


    @Data
    @ConfigurationProperties(prefix = "persistent.cache.config")
    public static class PoolConfig extends JedisPoolConfig {

    }
}
