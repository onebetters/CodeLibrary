package com.zzc.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * redis client 自动化配置
 * @author Administrator
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnBean(value = RedisProperties.class)
    public RedissonClient redissonClient(){
        Config config = new Config();
        // config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // config.useSingleServer().setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort());
        config.useSingleServer().setAddress(redisProperties.getUrl());
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        return Redisson.create(config);
    }

}
