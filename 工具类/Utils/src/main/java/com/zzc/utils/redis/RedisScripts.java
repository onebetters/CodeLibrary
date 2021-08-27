package com.zzc.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Slf4j
@Component
@Configuration
public class RedisScripts {

    public static RedisScript<Boolean> setIfAbsentAndExpireScript;

    @Bean
    public static RedisScript<Boolean> setIfAbsentAndExpireScript() {
        if (null != setIfAbsentAndExpireScript) {
            return setIfAbsentAndExpireScript;
        }

        synchronized (RedisScripts.class) {
            if (null != setIfAbsentAndExpireScript) {
                return setIfAbsentAndExpireScript;
            }

            DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/setIfAbsentExpire.lua")));
            redisScript.setResultType(Boolean.class);

            setIfAbsentAndExpireScript = redisScript;
        }

        return setIfAbsentAndExpireScript;
    }
}
