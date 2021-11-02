package com.zzc.collection.parallelStream;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author Administrator
 */
public class ParallelDoubleStreamSupport extends AbstractParallelStreamSupport<Double, DoubleStream> implements DoubleStream {

    ParallelDoubleStreamSupport(DoubleStream delegate, ForkJoinPool workerPool) {
        super(delegate, workerPool);
    }

    public static DoubleStream parallelStream(double[] array, ForkJoinPool workerPool) {
        Objects.requireNonNull(array, "Array must not be null");

        return new ParallelDoubleStreamSupport(Arrays.stream(array).parallel(), workerPool);
    }

    public static DoubleStream parallelStream(Spliterator.OfDouble spliterator, ForkJoinPool workerPool) {
        Objects.requireNonNull(spliterator, "Spliterator must not be null");

        return new ParallelDoubleStreamSupport(StreamSupport.doubleStream(spliterator, true), workerPool);
    }

    public static DoubleStream parallelStream(Supplier<? extends Spliterator.OfDouble> supplier, int characteristics, ForkJoinPool workerPool) {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return new ParallelDoubleStreamSupport(StreamSupport.doubleStream(supplier, characteristics, true), workerPool);
    }

    public static DoubleStream parallelStream(Builder builder, ForkJoinPool workerPool) {
        Objects.requireNonNull(builder, "Builder must not be null");

        return new ParallelDoubleStreamSupport(builder.build().parallel(), workerPool);
    }

    public static DoubleStream iterate(double seed, DoubleUnaryOperator operator, ForkJoinPool workerPool) {
        Objects.requireNonNull(operator, "Operator must not be null");

        return new ParallelDoubleStreamSupport(DoubleStream.iterate(seed, operator).parallel(), workerPool);
    }

    public static DoubleStream generate(DoubleSupplier supplier, ForkJoinPool workerPool) {
        Objects.requireNonNull(supplier, "Supplier must not be null");

        return new ParallelDoubleStreamSupport(DoubleStream.generate(supplier).parallel(), workerPool);
    }

    public static DoubleStream concat(DoubleStream a, DoubleStream b, ForkJoinPool workerPool) {
        Objects.requireNonNull(a, "Stream a must not be null");
        Objects.requireNonNull(b, "Stream b must not be null");

        return new ParallelDoubleStreamSupport(DoubleStream.concat(a, b).parallel(), workerPool);
    }

    @Override
    public DoubleStream filter(DoublePredicate predicate) {
        this.delegate = this.delegate.filter(predicate);
        return this;
    }

    @Override
    public DoubleStream map(DoubleUnaryOperator mapper) {
        this.delegate = this.delegate.map(mapper);
        return this;
    }

    @Override
    public <U> Stream<U> mapToObj(DoubleFunction<? extends U> mapper) {
        return new ParallelStreamSupport<>(this.delegate.mapToObj(mapper), this.workerPool);
    }

    @Override
    public IntStream mapToInt(DoubleToIntFunction mapper) {
        return new ParallelIntStreamSupport(this.delegate.mapToInt(mapper), this.workerPool);
    }

    @Override
    public LongStream mapToLong(DoubleToLongFunction mapper) {
        return new ParallelLongStreamSupport(this.delegate.mapToLong(mapper), this.workerPool);
    }

    @Override
    public DoubleStream flatMap(DoubleFunction<? extends DoubleStream> mapper) {
        this.delegate = this.delegate.flatMap(mapper);
        return this;
    }

    @Override
    public DoubleStream distinct() {
        this.delegate = this.delegate.distinct();
        return this;
    }

    @Override
    public DoubleStream sorted() {
        this.delegate = this.delegate.sorted();
        return this;
    }

    @Override
    public DoubleStream peek(DoubleConsumer action) {
        this.delegate = this.delegate.peek(action);
        return this;
    }

    @Override
    public DoubleStream limit(long maxSize) {
        this.delegate = this.delegate.limit(maxSize);
        return this;
    }

    @Override
    public DoubleStream skip(long n) {
        this.delegate = this.delegate.skip(n);
        return this;
    }

    @Override
    public void forEach(DoubleConsumer action) {
        execute(() -> this.delegate.forEach(action));
    }

    @Override
    public void forEachOrdered(DoubleConsumer action) {
        execute(() -> this.delegate.forEachOrdered(action));
    }

    @Override
    public double[] toArray() {
        return execute(() -> this.delegate.toArray());
    }

    @Override
    public double reduce(double identity, DoubleBinaryOperator op) {
        return execute(() -> this.delegate.reduce(identity, op));
    }

    @Override
    public OptionalDouble reduce(DoubleBinaryOperator op) {
        return execute(() -> this.delegate.reduce(op));
    }

    @Override
    public <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return execute(() -> this.delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public double sum() {
        return execute(() -> this.delegate.sum());
    }

    @Override
    public OptionalDouble min() {
        return execute(() -> this.delegate.min());
    }

    @Override
    public OptionalDouble max() {
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
    public DoubleSummaryStatistics summaryStatistics() {
        return execute(() -> this.delegate.summaryStatistics());
    }

    @Override
    public boolean anyMatch(DoublePredicate predicate) {
        return execute(() -> this.delegate.anyMatch(predicate));
    }

    @Override
    public boolean allMatch(DoublePredicate predicate) {
        return execute(() -> this.delegate.allMatch(predicate));
    }

    @Override
    public boolean noneMatch(DoublePredicate predicate) {
        return execute(() -> this.delegate.noneMatch(predicate));
    }

    @Override
    public OptionalDouble findFirst() {
        return execute(() -> this.delegate.findFirst());
    }

    @Override
    public OptionalDouble findAny() {
        return execute(() -> this.delegate.findAny());
    }

    @Override
    public Stream<Double> boxed() {
        return new ParallelStreamSupport<>(this.delegate.boxed(), this.workerPool);
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return this.delegate.spliterator();
    }
}
