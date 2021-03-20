package com.example.aggregator;

import com.example.other.StatTaskIds;
import com.example.event.Event;
import com.example.other.StatConstants;
import com.example.other.StatHelpers;
import com.example.utils.redis.RedisUtils;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Slf4j
public abstract class AbstractSimpleAggregator<S extends Event, V> implements Aggregator {

    private final String taskId;
    private final long periodInMinutes;
    private final AggEventReferee<S, V> eventReferee;

    public AbstractSimpleAggregator(StatTaskIds taskId, AggEventReferee<S, V> eventReferee) {
        this.taskId = taskId.getId();
        this.periodInMinutes = taskId.getPeriodInMinutes();
        this.eventReferee = eventReferee;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> boolean support( T source) {
        final Class<S> eventType = eventReferee.getEventType();
        return Objects.equals(eventType, source.getClass()) && this.doSupport((S) source);
    }

    @SuppressWarnings("unused")
    protected boolean doSupport( S source) {
        return true;
    }

    @Override
    public <T extends Event> void aggregate( T source) {
        this.aggregate(source.getBaseTime());
    }

    @Override
    public void aggregate( final LocalDateTime baseTime) {
        final Collection<S> sources = this.pullTodayAllMetadata(baseTime);
        if (CollectionUtils.isEmpty(sources)) {
            log.debug("metadataList is Empty, taskId={}, decider={}, sources={}", taskId, eventReferee, sources);
            return;
        }
        log.debug("sources={}", sources);

        final Map<String, List<S>> metadataListGroupByBulk = sources.stream().collect(Collectors.groupingBy(this::genBucketKey, Collectors.toList()));
        metadataListGroupByBulk.forEach(this::aggregateByBucketKey);
    }

    private void aggregateByBucketKey(final String bucketKey, final Collection<S> sources) {
        final Map<String, List<S>> groupByKey = this.groupByKey(sources);
        final Map<String, Number> valuesGroupByKey = groupByKey.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entity -> doAggregate(valueStream(entity.getValue()))));
        //RedisUtils.hashPutAll(bucketKey, valuesGroupByKey);
        //RedisUtils.expire(bucketKey, 2, TimeUnit.DAYS);
    }

    
    protected abstract Number doAggregate( final Stream<V> valuesUnderSameKey);

    protected Collection<S> pullTodayAllMetadata( LocalDateTime baseTime) {
        final String mainBucketKey = StatHelpers.eventBucketKey(eventReferee.getEventType(), baseTime);
        final Map<String, S> entries = RedisUtils.hashEntries(mainBucketKey);
        return entries.values();
    }

    
    protected String genBucketKey( S source) {
        final LocalDateTime baseTime = source.getBaseTime();
        final long taskBucketKeySuffix = StatHelpers.periodOffset(periodInMinutes, baseTime);
        return StatHelpers.taskBucketKey(taskId, periodInMinutes, taskBucketKeySuffix, eventReferee.getBucketSuffixSupplier().apply(source));
    }

    
    protected Map<String, List<S>> groupByKey( final Collection<S> sources) {
        return sources.stream()
                .filter(m -> eventReferee.getFilter().test(m))
                .filter(m -> Objects.nonNull(memberValue(m)))
                .flatMap(s -> this.memberKey(s).stream().map(k -> new KeyAndSource<>(k, s)))
                .collect(Collectors.groupingBy(KeyAndSource::getKey, Collectors.mapping(KeyAndSource::getSource, Collectors.toList())));
    }

    @Value
    private static class KeyAndSource<S> {
        String key;
        S source;
    }

    
    private Collection<String> memberKey( S source) {
        if (CollectionUtils.isEmpty(eventReferee.getKeySupplier().apply(source))) {
            return Collections.singleton(StatConstants.NULL_MEMBER_KEY);
        }
        return eventReferee.getKeySupplier()
                .apply(source)
                .stream()
                .map(key -> StringUtils.defaultIfBlank(key, StatConstants.NULL_MEMBER_KEY))
                .distinct()
                .collect(Collectors.toList());
    }

    private V memberValue( S source) {
        return eventReferee.getValueSupplier().apply(source);
    }

    
    protected Stream<V> valueStream( final Collection<S> sources) {
        return sources.stream().map(this::memberValue).filter(Objects::nonNull);
    }
}
