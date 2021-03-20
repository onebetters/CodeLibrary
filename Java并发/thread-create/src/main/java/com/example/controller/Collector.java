package com.example.controller;

import com.example.event.Event;

/**
 * @author Administrator
 */
public interface Collector {

    <T extends Event> void collect(T source);
}
