package com.zzc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import com.zzc.utils.function.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 异步执行器
 * @author Administrator
 */
@Slf4j
@Component
public class AsyncUtils {

    private static       AsyncTaskExecutor executor;
    private static       AsyncTaskExecutor subExecutor;
    private static final long              DEFAULT_TIMEOUT_SECONDS = 30;

    @Resource
    @Qualifier("pwbExecutor")
    public void setExecutor(final AsyncTaskExecutor executor) {
        AsyncUtils.executor = executor;
    }

    /**
     * 子线程池，用于避免异步线程池嵌套导致的资源死锁
     */
    @Resource
    @Qualifier("subPwbExecutor")
    public void setSubExecutor(final AsyncTaskExecutor executor) {
        AsyncUtils.subExecutor = executor;
    }

    private static <V> CompletableFuture<V> supply(@Nonnull Supplier<V> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    private static <V> CompletableFuture<V> subSupply(@Nonnull Supplier<V> supplier) {
        return CompletableFuture.supplyAsync(supplier, subExecutor);
    }

    public static CompletableFuture<Void> run(@Nonnull Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    public static <V> CompletableFuture<V> completed(@Nullable V value) {
        return CompletableFuture.completedFuture(value);
    }

    /**
     * 并发执行时，请注意自行评估及确保线程安全问题
     */
    @SuppressWarnings("unchecked")
    public static <V> List<V> parallel(@Nonnull final Collection<CompletableFuture<V>> futures) throws CompletionNestedException {
        return parallel(futures.toArray(new CompletableFuture[0]));
    }

    /**
     * 并发执行时，请注意自行评估及确保线程安全问题
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <V> List<V> parallel(@Nonnull final CompletableFuture<V>... futures) throws CompletionNestedException {
        final CompletableFuture<Void> done = CompletableFuture.allOf(futures);
        try {
            return done.thenApply(v -> Arrays.stream(futures).map(c -> {
                try {
                    return c.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
            }).collect(Collectors.toList())).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, R> R parallel(@Nonnull Supplier<T1> supplier1, @Nonnull Supplier<T2> supplier2, @Nonnull BiFunction<T1, T2, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        try {
            return CompletableFuture.allOf(t1Future, t2Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, R> R subParallel(@Nonnull Supplier<T1> supplier1, @Nonnull Supplier<T2> supplier2, @Nonnull BiFunction<T1, T2, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.subSupply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.subSupply(supplier2);
        try {
            return CompletableFuture.allOf(t1Future, t2Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, R> R parallel(@Nonnull Supplier<T1> supplier1,
            @Nonnull Supplier<T2> supplier2,
            @Nonnull Supplier<T3> supplier3,
            @Nonnull Function3<T1, T2, T3, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        try {
            return CompletableFuture.allOf(t1Future, t2Future, t3Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2, t3);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4, T5, T6> void parallel(Supplier<T1> supplier1,
                                                     Supplier<T2> supplier2,
                                                     Supplier<T3> supplier3,
                                                     Supplier<T4> supplier4,
                                                     Supplier<T5> supplier5,
                                                     Supplier<T6> supplier6,
                                                     final Consumer6<T1, T2, T3, T4, T5, T6> consumer) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        final CompletableFuture<T5> t5Future = AsyncUtils.supply(supplier5);
        final CompletableFuture<T6> t6Future = AsyncUtils.supply(supplier6);
        try {
            CompletableFuture.allOf(t1Future, t2Future).thenAccept(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                final T5 t5;
                final T6 t6;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t5 = t5Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t6 = t6Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                consumer.accept(t1, t2, t3, t4, t5, t6);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4, R> R parallel(@Nonnull Supplier<T1> supplier1,
            @Nonnull Supplier<T2> supplier2,
            @Nonnull Supplier<T3> supplier3,
            @Nonnull Supplier<T4> supplier4,
            @Nonnull Function4<T1, T2, T3, T4, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        try {
            return CompletableFuture.allOf(t1Future, t2Future, t3Future, t4Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2, t3, t4);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4, T5, R> R parallel(@Nonnull Supplier<T1> supplier1,
            @Nonnull Supplier<T2> supplier2,
            @Nonnull Supplier<T3> supplier3,
            @Nonnull Supplier<T4> supplier4,
            @Nonnull Supplier<T5> supplier5,
            @Nonnull Function5<T1, T2, T3, T4, T5, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        final CompletableFuture<T5> t5Future = AsyncUtils.supply(supplier5);
        try {
            return CompletableFuture.allOf(t1Future, t2Future, t3Future, t4Future, t5Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                final T5 t5;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t5 = t5Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2, t3, t4, t5);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4, T5, T6, R> R parallel(@Nonnull Supplier<T1> supplier1,
            @Nonnull Supplier<T2> supplier2,
            @Nonnull Supplier<T3> supplier3,
            @Nonnull Supplier<T4> supplier4,
            @Nonnull Supplier<T5> supplier5,
            @Nonnull Supplier<T6> supplier6,
            @Nonnull Function6<T1, T2, T3, T4, T5, T6, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        final CompletableFuture<T5> t5Future = AsyncUtils.supply(supplier5);
        final CompletableFuture<T6> t6Future = AsyncUtils.supply(supplier6);
        try {
            return CompletableFuture.allOf(t1Future, t2Future, t3Future, t4Future, t5Future, t6Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                final T5 t5;
                final T6 t6;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t5 = t5Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t6 = t6Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2, t3, t4, t5, t6);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> R parallel(@Nonnull Supplier<T1> supplier1,
            @Nonnull Supplier<T2> supplier2,
            @Nonnull Supplier<T3> supplier3,
            @Nonnull Supplier<T4> supplier4,
            @Nonnull Supplier<T5> supplier5,
            @Nonnull Supplier<T6> supplier6,
            @Nonnull Supplier<T7> supplier7,
            @Nonnull Function7<T1, T2, T3, T4, T5, T6, T7, R> function) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        final CompletableFuture<T5> t5Future = AsyncUtils.supply(supplier5);
        final CompletableFuture<T6> t6Future = AsyncUtils.supply(supplier6);
        final CompletableFuture<T7> t7Future = AsyncUtils.supply(supplier7);
        try {
            return CompletableFuture.allOf(t1Future, t2Future, t3Future, t4Future, t5Future, t6Future, t7Future).thenApply(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                final T5 t5;
                final T6 t6;
                final T7 t7;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t5 = t5Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t6 = t6Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t7 = t7Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                return function.apply(t1, t2, t3, t4, t5, t6, t7);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2> void parallelConsumer(Supplier<T1> supplier1, Supplier<T2> supplier2, final BiConsumer<T1, T2> consumer) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        try {
            CompletableFuture.allOf(t1Future, t2Future).thenAccept(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                consumer.accept(t1, t2);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3> void parallelConsumer(Supplier<T1> supplier1, Supplier<T2> supplier2, Supplier<T3> supplier3, final Consumer3<T1, T2, T3> consumer)
            throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        try {
            CompletableFuture.allOf(t1Future, t2Future).thenAccept(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                consumer.accept(t1, t2, t3);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4> void parallelConsumer(Supplier<T1> supplier1,
            Supplier<T2> supplier2,
            Supplier<T3> supplier3,
            Supplier<T4> supplier4,
            final Consumer4<T1, T2, T3, T4> consumer) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        try {
            CompletableFuture.allOf(t1Future, t2Future).thenAccept(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                consumer.accept(t1, t2, t3, t4);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    public static <T1, T2, T3, T4, T5> void parallelConsumer(Supplier<T1> supplier1,
            Supplier<T2> supplier2,
            Supplier<T3> supplier3,
            Supplier<T4> supplier4,
            Supplier<T5> supplier5,
            final Consumer5<T1, T2, T3, T4, T5> consumer) throws CompletionNestedException {
        final CompletableFuture<T1> t1Future = AsyncUtils.supply(supplier1);
        final CompletableFuture<T2> t2Future = AsyncUtils.supply(supplier2);
        final CompletableFuture<T3> t3Future = AsyncUtils.supply(supplier3);
        final CompletableFuture<T4> t4Future = AsyncUtils.supply(supplier4);
        final CompletableFuture<T5> t5Future = AsyncUtils.supply(supplier5);
        try {
            CompletableFuture.allOf(t1Future, t2Future).thenAccept(ignoredVoid -> {
                final T1 t1;
                final T2 t2;
                final T3 t3;
                final T4 t4;
                final T5 t5;
                try {
                    t1 = t1Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t2 = t2Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t3 = t3Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t4 = t4Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    t5 = t5Future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new CompletionNestedException(e);
                }
                consumer.accept(t1, t2, t3, t4, t5);
            }).get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CompletionNestedException(e);
        }
    }

    /**
     * 并发执行时，请注意自行评估及确保线程安全问题
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <V> List<V> parallel(@Nonnull final Supplier<V>... suppliers) throws CompletionNestedException {
        final Collection<CompletableFuture<V>> futures = Arrays.stream(suppliers).map(AsyncUtils::supply).collect(Collectors.toList());
        return parallel(futures);
    }

    /**
     * 并发执行时，请注意自行评估及确保线程安全问题
     */
    public static void parallel(@Nonnull final Runnable... runnable) throws CompletionNestedException {
        final List<CompletableFuture<Void>> futures = Arrays.stream(runnable).map(AsyncUtils::run).collect(Collectors.toList());
        parallel(futures);
    }

    /**
     * 并发执行时，请注意自行评估及确保线程安全问题
     */
    public static void parallel(@Nonnull final List<Runnable> runnableList) throws CompletionNestedException {
        final List<CompletableFuture<Void>> futures = runnableList.stream().map(AsyncUtils::run).collect(Collectors.toList());
        parallel(futures);
    }

    public static <T> Future<T> call(final Callable<T> callable) {
        return executor.submit(callable);
    }

    public static <T, F extends Future<T>> T get(F future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException(e);
        } catch (ExecutionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T, F extends Future<T>> T get(F future, long milliseconds) {
        try {
            return future.get(milliseconds, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException(e);
        } catch (ExecutionException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T, F extends Future<T>> T getElseNull(final F future) {
        return getElse(future, null);
    }

    public static <T, F extends Future<T>> T getElse(final F future, final T defaultValue) {
        try {
            if (future != null) {
                final T origValue = future.get();
                return Optional.ofNullable(origValue).orElse(defaultValue);
            }
        } catch (Exception e) {
            log.error("从Future中取值失败：" + e.getMessage(), e);
        }
        return defaultValue;
    }

    public static Throwable pickNestedAsyncException(final Throwable e) {
        if (e instanceof CompletionException) {
            return pickNestedAsyncException(e.getCause());
        }
        if (e instanceof CompletionNestedException) {
            return pickNestedAsyncException(e.getCause());
        }
        if (e instanceof ExecutionException) {
            return pickNestedAsyncException(e.getCause());
        }
        return e;
    }

    public static class CompletionNestedException extends RuntimeException {

        public CompletionNestedException(Throwable cause) {
            super(cause);
        }
    }
}
