package com.zzc.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * @author Administrator
 */
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class LazyFunction<T, R> implements Function<T, R> {

    private static final LazyFunction<?, ?> EMPTY = new LazyFunction<>(param -> null, null, true);

    private final Function<T, R> function;
    private R value = null;
    private volatile boolean resolved = false;

    public static <T, R> LazyFunction<T, R> of(Function<T, R> function) {
        return new LazyFunction<>(function);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> LazyFunction<T, R> empty() {
        return (LazyFunction<T, R>) EMPTY;
    }


    @Override
    public R apply(T param) {
        R value = this.value;
        if (this.resolved) {
            return value;
        }
        synchronized (function) {
            if (!this.resolved) {
                value = function.apply(param);
                this.value = value;
                this.resolved = true;
            }
        }
        return value;
    }
}