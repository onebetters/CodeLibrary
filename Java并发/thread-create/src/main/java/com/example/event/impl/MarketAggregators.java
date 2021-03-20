package com.example.event.impl;

import com.example.aggregator.AggEventReferee;
import com.example.aggregator.Aggregator;
import com.example.aggregator.imp.DistinctCountAggregator;
import com.example.event.MarketAdd;
import com.example.event.ShopChange;
import com.example.other.StatTaskIds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

/**
 * 新增市场
 * @author Administrator
 */
@Configuration
public class MarketAggregators {

    final Function<MarketAdd, String> valueSupplier = ShopChange::getShopId;

    /**
     * 每小时新增市场数
     */
    @Bean
    public Aggregator marketIncrHourlyQm() {
        return aggregator(StatTaskIds.MARKET_INCR_PER_HOUR_4_QM, s -> null);
    }

    /**
     * 当天新增市场数
     */
    @Bean
    public Aggregator marketIncrDailyQm() {
        return aggregator(StatTaskIds.MARKET_INCR_PER_DAY_4_QM, s -> null);
    }

    /**
     * 每小时新增市场数
     */
    @Bean
    public Aggregator marketIncrHourlyApp() {
        return aggregator(StatTaskIds.MARKET_INCR_PER_HOUR_4_APP, MarketAdd::getAppId);
    }

    /**
     * 当天新增市场数
     */
    @Bean
    public Aggregator marketIncrDailyApp() {
        return aggregator(StatTaskIds.MARKET_INCR_PER_DAY_4_APP, MarketAdd::getAppId);
    }

    private Aggregator aggregator(final StatTaskIds aggTaskId, final Function<MarketAdd, String> keySupplier) {
        return new DistinctCountAggregator<>(aggTaskId, AggEventReferee.of(MarketAdd.class, keySupplier, valueSupplier));
    }

    private Aggregator aggregator(final StatTaskIds aggTaskId, final Function<MarketAdd, String> keySupplier, final Function<MarketAdd, String> bucketSliceKeySupplier) {
        return new DistinctCountAggregator<>(aggTaskId, AggEventReferee.of(MarketAdd.class, keySupplier, valueSupplier, bucketSliceKeySupplier));
    }
}
