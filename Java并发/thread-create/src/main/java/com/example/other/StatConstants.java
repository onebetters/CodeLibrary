package com.example.other;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface StatConstants {

    String        KEY_DELIMITER        = ":";
    String        BUCKET_BIZ_DELIMITER = "|";
    String        PREFIX_BASE          = "AGG";
    String        PREFIX_EVENT         = String.join(KEY_DELIMITER, PREFIX_BASE, "EVENT");
    String        PREFIX_TASK          = String.join(KEY_DELIMITER, PREFIX_BASE, "TASK");
    String        NULL_MEMBER_KEY      = "_UNKNOWN";
    LocalDateTime START_DATE_TIME      = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
}
