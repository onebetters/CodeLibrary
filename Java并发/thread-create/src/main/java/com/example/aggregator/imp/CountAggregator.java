package com.example.aggregator.imp;

import com.example.aggregator.AbstractSimpleAggregator;
import com.example.aggregator.AggEventReferee;
import com.example.event.Event;
import com.example.other.StatTaskIds;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Slf4j
public class CountAggregator<S extends Event, V> extends AbstractSimpleAggregator<S, V> {

    public CountAggregator(StatTaskIds taskId, AggEventReferee<S, V> decider) {
        super(taskId, decider);
    }

    @Override
    protected Number doAggregate(Stream<V> valuesUnderSameKey) {
        return valuesUnderSameKey.count();
    }
}
