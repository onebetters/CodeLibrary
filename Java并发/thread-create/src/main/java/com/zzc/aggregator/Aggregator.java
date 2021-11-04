package com.zzc.aggregator;


import com.zzc.event.Event;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface Aggregator {

    <T extends Event> boolean support(T source);

    <T extends Event> void aggregate(T source);

    void aggregate(LocalDateTime baseTime);
}
