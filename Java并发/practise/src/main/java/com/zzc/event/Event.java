package com.zzc.event;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface Event {

    String getIdentifier();

    LocalDateTime getBaseTime();
}
