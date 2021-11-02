package com.zzc.async.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = AsyncProperties.ASYNC_PREFIX)
public class AsyncProperties {
    public static final String ASYNC_PREFIX = "async";

    private int corePoolSize = 2;
    private int maxPoolSize = 5;
    private int queueCapacity = 10000;
    private String threadNamePrefix = "my-async-executor-";
}
