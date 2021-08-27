package com.zzc.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 */
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Lazy<T> implements Supplier<T> {
    private static final Lazy<?> EMPTY = new Lazy<>(() -> null, null, true);

    private final Supplier<? extends T> supplier;
    private T value = null;
    private volatile boolean resolved = false;

    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<>(supplier);
    }

    public static <T> Lazy<T> of(T value) {
        Validate.notNull(value, "value must not be null!");
        return new Lazy<>(() -> value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> empty() {
        return (Lazy<T>) EMPTY;
    }

    public T get() {
        return getNullable();
    }

    public Optional<T> getOptional() {
        return Optional.ofNullable(getNullable());
    }

    public Lazy<T> or(Supplier<? extends T> supplier) {
        Validate.notNull(supplier, "supplier must not be null!");
        return Lazy.of(() -> orElseGet(supplier));
    }

    public Lazy<T> or(T value) {
        Validate.notNull(value, "value must not be null!");
        return Lazy.of(() -> orElse(value));
    }

    @Nullable
    public T orElse(@Nullable T value) {
        final T nullable = getNullable();
        return nullable == null ? value : nullable;
    }

    @Nullable
    private T orElseGet(Supplier<? extends T> supplier) {
        Validate.notNull(supplier, "supplier must not be null!");
        final T value = getNullable();
        return value == null ? supplier.get() : value;
    }

    public <S> Lazy<S> map(Function<? super T, ? extends S> function) {
        Validate.notNull(function, "function must not be null!");
        return Lazy.of(() -> {
            final T value = this.get();
            return Objects.isNull(value) ? null : function.apply(value);
        });
    }

    public <S> Lazy<S> flatMap(Function<? super T, Lazy<? extends S>> function) {
        Validate.notNull(function, "function must not be null!");
        return Lazy.of(() -> {
            final T value = this.get();
            return Objects.isNull(value) ? null : function.apply(value).get();
        });
    }

    @Nullable
    private T getNullable() {
        T value = this.value;
        if (this.resolved) {
            return value;
        }
        synchronized (supplier) {
            if (!this.resolved) {
                value = supplier.get();
                this.value = value;
                this.resolved = true;
            }
        }
        return value;
    }
}