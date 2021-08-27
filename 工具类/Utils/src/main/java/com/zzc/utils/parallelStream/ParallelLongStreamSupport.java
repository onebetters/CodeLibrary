package com.zzc.utils.parallelStream;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author Administrator
 */
public class ParallelLongStreamSupport extends AbstractParallelStreamSupport<Long, LongStream> implements LongStream {

    ParallelLongStreamSupport(LongStream delegate, ForkJoinPool workerPool) {
        super(delegate, workerPool);
    }

    public static LongStream parallelStream(long[] array, ForkJoinPool workerPool) {
        Objects.requireNonNull(array, "Array must not be null");

        return new ParallelLongStreamSupport(Arrays.stream(array).parallel(), workerPool);
    }

    public static LongStream parallelStream(Spliterator.OfLong spliterator, ForkJoinPool workerPool) {
        Objects.requireNonNull(spliterator, "Spliterator must not be null");

        return new ParallelLongStreamSupport(StreamSupport.longStream(spliterator, true), workerPool);
    }

    public static LongStream parallelStream(Supplier<? extends Spliterator.OfLong> supplier, int characteristics, ForkJoinPool workerPool) {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return new ParallelLongStreamSupport(StreamSupport.longStream(supplier, characteristics, true), workerPool);
    }

    public static LongStream parallelStream(Builder builder, ForkJoinPool workerPool) {
        Objects.requireNonNull(builder, "Builder must not be null");

        return new ParallelLongStreamSupport(builder.build().parallel(), workerPool);
    }

    public static LongStream iterate(long seed, LongUnaryOperator operator, ForkJoinPool workerPool) {
        Objects.requireNonNull(operator, "Operator must not be null");

        return new ParallelLongStreamSupport(LongStream.iterate(seed, operator).parallel(), workerPool);
    }

    public static LongStream generate(LongSupplier supplier, ForkJoinPool workerPool) {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return new ParallelLongStreamSupport(LongStream.generate(supplier).parallel(), workerPool);
    }

    public static LongStream range(long startInclusive, long endExclusive, ForkJoinPool workerPool) {
        return new ParallelLongStreamSupport(LongStream.range(startInclusive, endExclusive).parallel(), workerPool);
    }

    public static LongStream rangeClosed(long startInclusive, long endInclusive, ForkJoinPool workerPool) {
        return new ParallelLongStreamSupport(LongStream.rangeClosed(startInclusive, endInclusive).parallel(), workerPool);
    }

    public static LongStream concat(LongStream a, LongStream b, ForkJoinPool workerPool) {
        Objects.requireNonNull(a, "Stream a must not be null");
        Objects.requireNonNull(b, "Stream b must not be null");

        return new ParallelLongStreamSupport(LongStream.concat(a, b).parallel(), workerPool);
    }

    @Override
    public LongStream filter(LongPredicate predicate) {
        this.delegate = this.delegate.filter(predicate);
        return this;
    }

    @Override
    public LongStream map(LongUnaryOperator mapper) {
        this.delegate = this.delegate.map(mapper);
        return this;
    }

    @Override
    public <U> Stream<U> mapToObj(LongFunction<? extends U> mapper) {
        return new ParallelStreamSupport<>(this.delegate.mapToObj(mapper), this.workerPool);
    }

    @Override
    public IntStream mapToInt(LongToIntFunction mapper) {
        return new ParallelIntStreamSupport(this.delegate.mapToInt(mapper), this.workerPool);
    }

    @Override
    public DoubleStream mapToDouble(LongToDoubleFunction mapper) {
        return new ParallelDoubleStreamSupport(this.delegate.mapToDouble(mapper), this.workerPool);
    }

    @Override
    public LongStream flatMap(LongFunction<? extends LongStream> mapper) {
        this.delegate = this.delegate.flatMap(mapper);
        return this;
    }

    @Override
    public LongStream distinct() {
        this.delegate = this.delegate.distinct();
        return this;
    }

    @Override
    public LongStream sorted() {
        this.delegate = this.delegate.sorted();
        return this;
    }

    @Override
    public LongStream peek(LongConsumer action) {
        this.delegate = this.delegate.peek(action);
        return this;
    }

    @Override
    public LongStream limit(long maxSize) {
        this.delegate = this.delegate.limit(maxSize);
        return this;
    }

    @Override
    public LongStream skip(long n) {
        this.delegate = this.delegate.skip(n);
        return this;
    }

    @Override
    public void forEach(LongConsumer action) {
        execute(() -> this.delegate.forEach(action));
    }

    @Override
    public void forEachOrdered(LongConsumer action) {
        execute(() -> this.delegate.forEachOrdered(action));
    }

    @Override
    public long[] toArray() {
        return execute(() -> this.delegate.toArray());
    }

    @Override
    public long reduce(long identity, LongBinaryOperator op) {
        return execute(() -> this.delegate.reduce(identity, op));
    }

    @Override
    public OptionalLong reduce(LongBinaryOperator op) {
        return execute(() -> this.delegate.reduce(op));
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return execute(() -> this.delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public long sum() {
        return execute(() -> this.delegate.sum());
    }

    @Override
    public OptionalLong min() {
        return execute(() -> this.delegate.min());
    }

    @Override
    public OptionalLong max() {
        return execute(() -> this.delegate.max());
    }

    @Override
    public long count() {
        return execute(() -> this.delegate.count());
    }

    @Override
    public OptionalDouble average() {
        return execute(() -> this.delegate.average());
    }

    @Override
    public LongSummaryStatistics summaryStatistics() {
        return execute(() -> this.delegate.summaryStatistics());
    }

    @Override
    public boolean anyMatch(LongPredicate predicate) {
        return execute(() -> this.delegate.anyMatch(predicate));
    }

    @Override
    public boolean allMatch(LongPredicate predicate) {
        return execute(() -> this.delegate.allMatch(predicate));
    }

    @Override
    public boolean noneMatch(LongPredicate predicate) {
        return execute(() -> this.delegate.noneMatch(predicate));
    }

    @Override
    public OptionalLong findFirst() {
        return execute(() -> this.delegate.findFirst());
    }

    @Override
    public OptionalLong findAny() {
        return execute(() -> this.delegate.findAny());
    }

    @Override
    public DoubleStream asDoubleStream() {
        return new ParallelDoubleStreamSupport(this.delegate.asDoubleStream(), this.workerPool);
    }

    @Override
    public Stream<Long> boxed() {
        return new ParallelStreamSupport<>(this.delegate.boxed(), this.workerPool);
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return this.delegate.spliterator();
    }
}
