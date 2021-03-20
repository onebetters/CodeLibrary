package com.example.aggregator;


import com.example.event.Event;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface Aggregator {

    <T extends Event> boolean support(T source);

    <T extends Event> void aggregate(T source);

    void aggregate(LocalDateTime baseTime);
}
