package com.example.other;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * <p>Date: 2020-05-29 14:05.</p>
 *
 * @author <a href="mailto:baixiaolin@qianmi.com">OF2510-白晓林</a>
 * @version 0.1.0
 */
@Getter
public enum StatTaskIds {

    MARKET_INCR_PER_HOUR_4_QM("M_INC", true, TimeUnit.HOURS, "新增市场数"),
    MARKET_INCR_PER_DAY_4_QM("M_INC", true, TimeUnit.DAYS, "新增市场数"),
    MARKET_INCR_PER_HOUR_4_APP("M_INC", false, TimeUnit.HOURS, "新增市场数"),
    MARKET_INCR_PER_DAY_4_APP("M_INC", false, TimeUnit.DAYS, "新增市场数"),

    DISTINCT_MARKET_HAVE_TRADE_PER_HOUR_4_QM("M_TRA_INC", true, TimeUnit.HOURS, "交易市场数"),
    DISTINCT_MARKET_HAVE_TRADE_PER_DAY_4_QM("M_TRA_INC", true, TimeUnit.DAYS, "交易市场数"),
    DISTINCT_MARKET_HAVE_TRADE_PER_HOUR_4_APP("M_TRA_INC", false, TimeUnit.HOURS, "交易市场数"),
    DISTINCT_MARKET_HAVE_TRADE_PER_DAY_4_APP("M_TRA_INC", false, TimeUnit.DAYS, "交易市场数"),


    SELLER_INCR_PER_HOUR_4_QM("S_INC", true, TimeUnit.HOURS, "新增商家数"),
    SELLER_INCR_PER_DAY_4_QM("S_INC", true, TimeUnit.DAYS, "新增商家数"),
    SELLER_INCR_PER_HOUR_4_APP("S_INC", false, TimeUnit.HOURS, "新增商家数"),
    SELLER_INCR_PER_DAY_4_APP("S_INC", false, TimeUnit.DAYS, "新增商家数"),

    DISTINCT_SELLER_HAVE_TRADE_PER_HOUR_4_QM("S_TRA_INC", true, TimeUnit.HOURS, "交易商家数"),
    DISTINCT_SELLER_HAVE_TRADE_PER_DAY_4_QM("S_TRA_INC", true, TimeUnit.DAYS, "交易商家数"),
    DISTINCT_SELLER_HAVE_TRADE_PER_HOUR_4_APP("S_TRA_INC", false, TimeUnit.HOURS, "交易商家数"),
    DISTINCT_SELLER_HAVE_TRADE_PER_DAY_4_APP("S_TRA_INC", false, TimeUnit.DAYS, "交易商家数"),
    ;


    private final String  id;
    private final long    periodInMinutes;
    private final boolean slice;
    private final String  desc;

    StatTaskIds(String group, boolean isQm, TimeUnit timeUnit, String desc) {
        this(group, isQm, timeUnit, false, desc);
    }

    StatTaskIds(String group, boolean isQm, TimeUnit timeUnit, final boolean slice, String desc) {
        this.periodInMinutes = timeUnit.toMinutes(1);
        this.id = String.join("_", group, isQm ? "QM" : "APP", StringUtils.substring(timeUnit.name(), 0, 3));
        this.slice = slice;
        this.desc = desc;
    }

    public static StatTaskIds of(final String name) {
        return Arrays.stream(values()).filter(e -> StringUtils.equals(name, e.name()) || StringUtils.equalsIgnoreCase(name, e.getId())).findFirst().orElse(null);
    }
}
