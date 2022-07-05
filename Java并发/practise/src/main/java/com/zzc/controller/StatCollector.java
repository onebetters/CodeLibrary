package com.zzc.controller;

import com.zzc.AggEventBus;
import com.zzc.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatCollector {

    private final RedisCollector collector;
    private final AggEventBus    aggEventBus;

    public <T extends Event> void collect(final T source) {
        collector.collect(source);
        aggEventBus.send(source);
    }

    public void aggregateAll() {
        aggEventBus.aggregateAll();
    }
}
