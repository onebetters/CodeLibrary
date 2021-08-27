package com.zzc.utils;

import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Administrator
 */
public interface Optionals {

    static <T> void ifPresentOrElse(@Nonnull Optional<T> optional,
                                    @Nonnull Consumer<? super T> consumer,
                                    @Nonnull Runnable runnable) {
        Assert.notNull(optional, "Optional must not be null!");
        Assert.notNull(consumer, "Consumer must not be null!");
        Assert.notNull(runnable, "Runnable must not be null!");

        if (optional.isPresent()) {
            optional.ifPresent(consumer);
        } else {
            runnable.run();
        }
    }

    static <T, R> R ifPresentReturnOrDefault(@Nonnull Optional<T> optional,
                                             @Nonnull Function<T, R> supplier,
                                             @Nullable R defaultValue) {
        Assert.notNull(optional, "Optional must not be null!");
        Assert.notNull(supplier, "Supplier must not be null!");

        if (optional.isPresent()) {
            return supplier.apply(optional.get());
        } else {
            return defaultValue;
        }
    }

    static <T, R> R ifPresentReturnOrDefault(@Nonnull Optional<T> optional,
                                             @Nonnull Function<T, R> supplier,
                                             @Nonnull Supplier<R> defaultValueSupplier) {
        Assert.notNull(optional, "Optional must not be null!");
        Assert.notNull(supplier, "Supplier must not be null!");
        Assert.notNull(defaultValueSupplier, "Default value supplier must not be null!");

        if (optional.isPresent()) {
            return supplier.apply(optional.get());
        } else {
            return defaultValueSupplier.get();
        }
    }

    static <T, R, E extends Throwable> R ifPresentReturnOrThrow(@Nonnull Optional<T> optional,
                                                                @Nonnull Function<T, R> supplier,
                                                                @Nonnull Supplier<E> exceptionSupplier) throws E {
        Assert.notNull(optional, "Optional must not be null!");
        Assert.notNull(supplier, "Supplier must not be null!");
        Assert.notNull(exceptionSupplier, "Exception supplier must not be null!");

        if (optional.isPresent()) {
            return supplier.apply(optional.get());
        } else {
            throw exceptionSupplier.get();
        }
    }
}
