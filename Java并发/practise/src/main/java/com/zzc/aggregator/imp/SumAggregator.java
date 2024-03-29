package com.zzc.aggregator.imp;

import com.zzc.aggregator.AbstractSimpleAggregator;
import com.zzc.aggregator.AggEventReferee;
import com.zzc.event.Event;
import com.zzc.other.StatTaskIds;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Slf4j
public class SumAggregator<S extends Event> extends AbstractSimpleAggregator<S, Number> {

    public SumAggregator(StatTaskIds taskId, AggEventReferee<S, Number> decider) {
        super(taskId, decider);
    }

    @Override
    protected Number doAggregate( Stream<Number> valuesUnderSameKey) {
        return valuesUnderSameKey.reduce(this::add).orElse(BigDecimal.ZERO);
    }

    private BigDecimal add( final Number a,  final Number b) {
        return number2BigDecimal(a).add(number2BigDecimal(b));
    }

    private BigDecimal number2BigDecimal( final Number number) {
        return BigDecimal.valueOf(number.doubleValue());
    }
}
