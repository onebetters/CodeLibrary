package com.zzc.utils.parallelStream;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class PartitionStream {

    /**
     * 按固定每页最大大小进行分页Stream处理。每页最多不超过size条数据
     * 适用场景: 分页查询第三方数据（如中台接口或ES搜索引擎），pageSize大小有限制，大批量数据查询时需要分批查询
     */
    public <T> Stream<List<T>> partition(final Collection<T> collection, final int size) {
        return ParallelStreamSupport.parallelStream(SliceUtils.sliceByFixedSize(collection, size), new ForkJoinPool());
    }

    /**
     * 按固定每页最大大小进行分页Stream处理。每页最多不超过size条数据
     * 适用场景: 分页查询第三方数据（如中台接口或ES搜索引擎），pageSize大小有限制，大批量数据查询时需要分批查询
     */
    public <T> Stream<Set<T>> partition(final Set<T> collection, final int size) {
        return ParallelStreamSupport.parallelStream(SliceUtils.sliceByFixedSize(collection, size), new ForkJoinPool());
    }

    /**
     * 按固定数量进行分页Stream处理。将集合切分成size片数据，每片数据大小未知
     * 适用场景: 多线程并发数据切片处理
     */
    public <T> Stream<List<T>> fairPartition(final Collection<T> collection, final int size) {
        return ParallelStreamSupport.parallelStream(SliceUtils.slice(collection, size), new ForkJoinPool());
    }

    /**
     * 按固定数量进行分页Stream处理。将集合切分成size片数据，每片数据大小未知
     * 适用场景: 多线程并发数据切片处理
     */
    public <T> Stream<Set<T>> fairPartition(final Set<T> collection, final int size) {
        return ParallelStreamSupport.parallelStream(SliceUtils.slice(collection, size), new ForkJoinPool());
    }
}
