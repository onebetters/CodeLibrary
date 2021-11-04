package com.zzc.controller;

import com.zzc.event.Event;
import com.zzc.other.StatHelpers;
import com.zzc.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class RedisCollector implements Collector {

    @Override
    public <T extends Event> void collect(T source) {
        log.debug("Start add event={}", source);

        final String key = StatHelpers.eventBucketKey(source.getClass(), source.getBaseTime());
        final String id = source.getIdentifier();

        RedisUtils.hashPut(key, id, source);
        RedisUtils.expire(key, 2, TimeUnit.DAYS);

        log.debug("Finish add event={}", source);
    }
}
