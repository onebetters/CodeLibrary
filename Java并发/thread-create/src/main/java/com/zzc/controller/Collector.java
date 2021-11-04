package com.zzc.controller;

import com.zzc.event.Event;

/**
 * @author Administrator
 */
public interface Collector {

    <T extends Event> void collect(T source);
}
