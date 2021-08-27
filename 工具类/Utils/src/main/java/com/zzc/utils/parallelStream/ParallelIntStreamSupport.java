package com.zzc.utils.parallelStream;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author Administrator
 */
public class ParallelIntStreamSupport extends AbstractParallelStreamSupport<Integer, IntStream> implements IntStream {

    ParallelIntStreamSupport(IntStream delegate, ForkJoinPool workerPool) {
        super(delegate, workerPool);
    }

    public static IntStream parallelStream(int[] array, ForkJoinPool workerPool) {
        Objects.requireNonNull(array, "Array must not be null");

        return new ParallelIntStreamSupport(Arrays.stream(array).parallel(), workerPool);
    }

    public static IntStream parallelStream(Spliterator.OfInt spliterator, ForkJoinPool workerPool) {
        Objects.requireNonNull(spliterator, "Spliterator must not be null");

        return new ParallelIntStreamSupport(StreamSupport.intStream(spliterator, true), workerPool);
    }

    public static IntStream parallelStream(Supplier<? extends Spliterator.OfInt> supplier, int characteristics, ForkJoinPool workerPool) {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return new ParallelIntStreamSupport(StreamSupport.intStream(supplier, characteristics, true), workerPool);
    }

    public static IntStream parallelStream(Builder builder, ForkJoinPool workerPool) {
        Objects.requireNonNull(builder, "Builder must not be null");

        return new ParallelIntStreamSupport(builder.build().parallel(), workerPool);
    }

    public static IntStream iterate(int seed, IntUnaryOperator operator, ForkJoinPool workerPool) {
        Objects.requireNonNull(operator, "Operator must not be null");

        return new ParallelIntStreamSupport(IntStream.iterate(seed, operator).parallel(), workerPool);
    }

    public static IntStream generate(IntSupplier supplier, ForkJoinPool workerPool) {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return new ParallelIntStreamSupport(IntStream.generate(supplier).parallel(), workerPool);
    }

    public static IntStream range(int startInclusive, int endExclusive, ForkJoinPool workerPool) {
        return new ParallelIntStreamSupport(IntStream.range(startInclusive, endExclusive).parallel(), workerPool);
    }

    public static IntStream rangeClosed(int startInclusive, int endInclusive, ForkJoinPool workerPool) {
        return new ParallelIntStreamSupport(IntStream.rangeClosed(startInclusive, endInclusive).parallel(), workerPool);
    }

    public static IntStream concat(IntStream a, IntStream b, ForkJoinPool workerPool) {
        Objects.requireNonNull(a, "Stream a must not be null");
        Objects.requireNonNull(b, "Stream b must not be null");

        return new ParallelIntStreamSupport(IntStream.concat(a, b).parallel(), workerPool);
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        this.delegate = this.delegate.filter(predicate);
        return this;
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        this.delegate = this.delegate.map(mapper);
        return this;
    }

    @Override
    public <U> Stream<U> mapToObj(IntFunction<? extends U> mapper) {
        return new ParallelStreamSupport<U>(this.delegate.mapToObj(mapper), this.workerPool);
    }

    @Override
    public LongStream mapToLong(IntToLongFunction mapper) {
        return new ParallelLongStreamSupport(this.delegate.mapToLong(mapper), this.workerPool);
    }

    @Override
    public DoubleStream mapToDouble(IntToDoubleFunction mapper) {
        return new ParallelDoubleStreamSupport(this.delegate.mapToDouble(mapper), this.workerPool);
    }

    @Override
    public IntStream flatMap(IntFunction<? extends IntStream> mapper) {
        this.delegate = this.delegate.flatMap(mapper);
        return this;
    }

    @Override
    public IntStream distinct() {
        this.delegate = this.delegate.distinct();
        return this;
    }

    @Override
    public IntStream sorted() {
        this.delegate = this.delegate.sorted();
        return this;
    }

    @Override
    public IntStream peek(IntConsumer action) {
        this.delegate = this.delegate.peek(action);
        return this;
    }

    @Override
    public IntStream limit(long maxSize) {
        this.delegate = this.delegate.limit(maxSize);
        return this;
    }

    @Override
    public IntStream skip(long n) {
        this.delegate = this.delegate.skip(n);
        return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        execute(() -> this.delegate.forEach(action));
    }

    @Override
    public void forEachOrdered(IntConsumer action) {
        execute(() -> this.delegate.forEachOrdered(action));
    }

    @Override
    public int[] toArray() {
        return execute(() -> this.delegate.toArray());
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        return execute(() -> this.delegate.reduce(identity, op));
    }

    @Override
    public OptionalInt reduce(IntBinaryOperator op) {
        return execute(() -> this.delegate.reduce(op));
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return execute(() -> this.delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public int sum() {
        return execute(() -> this.delegate.sum());
    }

    @Override
    public OptionalInt min() {
        return execute(() -> this.delegate.min());
    }

    @Override
    public OptionalInt max() {
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
    public IntSummaryStatistics summaryStatistics() {
        return execute(() -> this.delegate.summaryStatistics());
    }

    @Override
    public boolean anyMatch(IntPredicate predicate) {
        return execute(() -> this.delegate.anyMatch(predicate));
    }

    @Override
    public boolean allMatch(IntPredicate predicate) {
        return execute(() -> this.delegate.allMatch(predicate));
    }

    @Override
    public boolean noneMatch(IntPredicate predicate) {
        return execute(() -> this.delegate.noneMatch(predicate));
    }

    @Override
    public OptionalInt findFirst() {
        return execute(() -> this.delegate.findFirst());
    }

    @Override
    public OptionalInt findAny() {
        return execute(() -> this.delegate.findAny());
    }

    @Override
    public LongStream asLongStream() {
        return new ParallelLongStreamSupport(this.delegate.asLongStream(), this.workerPool);
    }

    @Override
    public DoubleStream asDoubleStream() {
        return new ParallelDoubleStreamSupport(this.delegate.asDoubleStream(), this.workerPool);
    }

    @Override
    public Stream<Integer> boxed() {
        return new ParallelStreamSupport<>(this.delegate.boxed(), this.workerPool);
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return this.delegate.spliterator();
    }
}