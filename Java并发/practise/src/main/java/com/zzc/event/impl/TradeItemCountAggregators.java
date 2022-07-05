package com.zzc.event.impl;

import com.zzc.aggregator.AggEventReferee;
import com.zzc.aggregator.Aggregator;
import com.zzc.aggregator.imp.SumAggregator;
import com.zzc.event.MarketTradeItem;
import com.zzc.other.StatTaskIds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

/**
 * 市场成交商品数
 *
 * @author Administrator
 */
@Configuration
public class TradeItemCountAggregators {

    private final Function<MarketTradeItem, Number> valueSupplier = MarketTradeItem::getCount;

    /**
     * 市场商品销量（天）
     */
    @Bean
    public Aggregator tradeItemCountByMarketIdPerDay() {
        return aggregator(StatTaskIds.TRADE_ITEM_COUNT_BY_MARKET_ID_PER_DAY, MarketTradeItem::getSkuId, MarketTradeItem::getMarketId);
    }

    /**
     * 市场商品销量（小时）
     */
    @Bean
    public Aggregator tradeItemCountByMarketIdPerHour() {
        return aggregator(StatTaskIds.TRADE_ITEM_COUNT_BY_MARKET_ID_PER_HOUR, MarketTradeItem::getSkuId, MarketTradeItem::getMarketId);
    }

    private Aggregator aggregator(final StatTaskIds aggTaskId,
                                  final Function<MarketTradeItem, String> keySupplier,
                                  final Function<MarketTradeItem, String> bucketSliceKeySupplier) {
        return new SumAggregator<>(aggTaskId, AggEventReferee.of(MarketTradeItem.class, keySupplier, valueSupplier, bucketSliceKeySupplier));
    }
}
