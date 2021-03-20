package com.example.event.impl;

import com.example.aggregator.AggEventReferee;
import com.example.aggregator.Aggregator;
import com.example.aggregator.imp.SumAggregator;
import com.example.event.MarketTradeItem;
import com.example.other.StatTaskIds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.function.Function;

/**
 * 市场商品交易额
 *
 * @author Administrator
 */
@Configuration
public class TradeItemAmountSumAggregators {

    private final Function<MarketTradeItem, Number> valueSupplier = MarketTradeItem::getItemTotalPrice;

    /**
     * 市场商品交易金额
     */
    @Bean
    public Aggregator tradeItemAmountByMarketIdPerDay() {
        return aggregator(StatTaskIds.TRADE_ITEM_AMOUNT_BY_MARKET_ID_PER_DAY, MarketTradeItem::getSkuId, MarketTradeItem::getMarketId);
    }

    /**
     * 市场商品类目交易金额
     */
    @Bean
    public Aggregator tradeItemCatsAmountByMarketIdPerDay() {
        return aggregatorDupKey(StatTaskIds.TRADE_ITEM_CATS_AMOUNT_BY_MARKET_ID_PER_DAY, MarketTradeItem::getCatIds, MarketTradeItem::getMarketId);
    }

    private Aggregator aggregator(final StatTaskIds aggTaskId,
                                  final Function<MarketTradeItem, String> keySupplier,
                                  final Function<MarketTradeItem, String> bucketSliceKeySupplier) {
        return new SumAggregator<>(aggTaskId, AggEventReferee.of(MarketTradeItem.class, keySupplier, valueSupplier, bucketSliceKeySupplier));
    }

    private Aggregator aggregatorDupKey(final StatTaskIds aggTaskId,
                                  final Function<MarketTradeItem, Collection<String>> keySupplier,
                                  final Function<MarketTradeItem, String> bucketSliceKeySupplier) {
        return new SumAggregator<>(aggTaskId, AggEventReferee.of(MarketTradeItem.class, keySupplier, valueSupplier, bucketSliceKeySupplier, null));
    }
}
