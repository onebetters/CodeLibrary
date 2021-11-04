package com.zzc.other;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
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

    TRADE_ITEM_COUNT_BY_MARKET_ID_PER_DAY("P_TIC", true, TimeUnit.DAYS, true,"市场商品销量"),
    TRADE_ITEM_COUNT_BY_MARKET_ID_PER_HOUR("P_TIC", true, TimeUnit.HOURS, true, "市场商品销量"),

    TRADE_ITEM_AMOUNT_BY_MARKET_ID_PER_DAY("P_TIA", true, TimeUnit.DAYS, true,"市场商品交易金额"),
    TRADE_ITEM_CATS_AMOUNT_BY_MARKET_ID_PER_DAY("P_TICA", true, TimeUnit.DAYS, true,"市场商品类目交易金额"),
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
