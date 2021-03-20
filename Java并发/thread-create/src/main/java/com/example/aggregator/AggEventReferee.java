package com.example.aggregator;

import com.example.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Administrator
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AggEventReferee<T extends Event, V> {

    private final Class<T> eventType;
    private final Function<T, Collection<String>> keySupplier;
    private final Function<T, V> valueSupplier;
    private final Function<T, String> bucketSuffixSupplier;
    private final Predicate<T> filter;

    public static <T extends Event, V> AggEventReferee<T, V> of(
            Class<T> eventType,
            Function<T, String> keySupplier,
            Function<T, V> valueSupplier) {
        return of(eventType, keySupplier, valueSupplier, null);
    }

    public static <T extends Event, V> AggEventReferee<T, V> of(
            Class<T> eventType,
            Function<T, String> keySupplier,
            Function<T, V> valueSupplier,
            Function<T, String> bucketSliceKeySupplier) {
        return of(eventType, t -> Collections.singletonList(keySupplier.apply(t)), valueSupplier, bucketSliceKeySupplier, null);
    }

    public static <T extends Event, V> AggEventReferee<T, V> of(
            Class<T> eventType,
            Function<T, Collection<String>> keySupplier,
            Function<T, V> valueSupplier,
            Function<T, String> bucketSliceKeySupplier,
            Predicate<T> filter) {
        return new AggEventReferee<>(eventType,keySupplier,valueSupplier,Optional.ofNullable(bucketSliceKeySupplier).orElse(s -> null),
                                                                                                  Optional.ofNullable(filter).orElse(s -> true));
    }
}
