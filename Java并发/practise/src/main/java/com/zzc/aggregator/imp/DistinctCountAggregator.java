package com.zzc.aggregator.imp;

import com.zzc.aggregator.AbstractSimpleAggregator;
import com.zzc.aggregator.AggEventReferee;
import com.zzc.event.Event;
import com.zzc.other.StatTaskIds;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Slf4j
public class DistinctCountAggregator<S extends Event, V> extends AbstractSimpleAggregator<S, V> {

    public DistinctCountAggregator(StatTaskIds taskId, AggEventReferee<S, V> decider) {
        super(taskId, decider);
    }

    @Override
    protected Number doAggregate(Stream<V> valuesUnderSameKey) {
        return valuesUnderSameKey.distinct().count();
    }
}
