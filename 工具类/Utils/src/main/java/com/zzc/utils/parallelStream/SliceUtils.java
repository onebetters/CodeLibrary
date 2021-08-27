package com.zzc.utils.parallelStream;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ID系列分片工具
 */
@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class SliceUtils {

    /**
     * 公平切分数据段。最终被切分成segments段
     */
    public <T> List<List<T>> slice(final Collection<T> collection, final int segments) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }
        final int length = CollectionUtils.size(collection);
        if (1 == length) {
            return Collections.singletonList(new ArrayList<>(collection));
        } else {
            final int[] lastIndex = {0};
            return Arrays.stream(segmentsIndex(length, segments)).boxed().map(index -> slice(lastIndex, index, collection)).collect(Collectors.toList());
        }
    }

    private int[] segmentsIndex(final int length, final int segments) {
        final int[] indexes = new int[Math.min(length, segments)];
        Arrays.fill(indexes, length / segments);
        IntStream.range(0, length % segments).forEach(i -> indexes[i]++);
        for (int i = 1; i < indexes.length; i++) {
            indexes[i] += indexes[i - 1];
        }
        return indexes;
    }

    private <T> List<T> slice(final int[] lastIndex, final int index, final Collection<T> collection) {
        final List<T> c = collection.stream().skip(lastIndex[0]).limit((long) index - (long) lastIndex[0]).collect(Collectors.toList());
        lastIndex[0] = index;
        return c;
    }

    public <T> List<Set<T>> slice(final Set<T> collection, final int segments) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }
        final int length = CollectionUtils.size(collection);
        if (1 == length) {
            return Collections.singletonList(collection);
        } else {
            final int[] lastIndex = {0};
            return Arrays.stream(segmentsIndex(length, segments)).boxed().map(index -> slice(lastIndex, index, collection)).collect(Collectors.toList());
        }
    }

    private <T> Set<T> slice(final int[] lastIndex, final int index, final Set<T> collection) {
        final Set<T> c = collection.stream().skip(lastIndex[0]).limit((long) index - (long) lastIndex[0]).collect(Collectors.toSet());
        lastIndex[0] = index;
        return c;
    }

    /**
     * 按固定大小切分数据段，每片数量最多size个，总共多少段未知
     */
    public <T> List<List<T>> sliceByFixedSize(final Collection<T> collection, int size) {
        return IntStream.range(0, collection.size())
                .boxed()
                .filter(t -> t % size == 0)
                .map(t -> collection.stream().skip(t).limit(size).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    /**
     * 按固定大小切分数据段，每片数量最多size个，总共多少段未知
     */
    public <T> Set<Set<T>> sliceByFixedSize(final Set<T> collection, int size) {
        return IntStream.range(0, collection.size())
                .boxed()
                .filter(t -> t % size == 0)
                .map(t -> collection.stream().skip(t).limit(size).collect(Collectors.toSet()))
                .collect(Collectors.toSet());
    }
}
