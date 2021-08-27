package com.zzc.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void setRedisTemplate(final RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> T get(final String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static Long increment(final String key, final Long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    /**
     * @param timeout 超时时间 - 单位：秒
     */
    public static void set(final String key, final Object value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    public static void set(final String key, final Object value, long timeout, final TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public static void delete(final String key) {
        redisTemplate.delete(key);
    }

    public static boolean setIfAbsent(final String key, final Object value) {
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key, value)).orElse(false);
    }

    public static boolean setIfAbsent(final String key, final Object value, final long timeout, final TimeUnit unit) {
        final long rawTimeout = TimeoutUtils.toMillis(timeout, unit);
        return Optional.ofNullable(RedisUtils.redisTemplate.execute(RedisScripts.setIfAbsentAndExpireScript, Collections.singletonList(key), value, rawTimeout))
                .orElse(false);
    }

    public static boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    public static Boolean expire(final String key, final long timeout) {
        return RedisUtils.redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public static Boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return RedisUtils.redisTemplate.expire(key, timeout, unit);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> getSet(final String key) {
        final Set<Object> members = redisTemplate.opsForSet().members(key);
        if (Objects.isNull(members)) {
            return Collections.emptySet();
        } else {
            return (Set<T>) members;
        }
    }

    public static <T> void addSet(final String key, final T member) {
        redisTemplate.opsForSet().add(key, member);
    }

    public static <T> void addSet(final String key, final Collection<T> members) {
        if (CollectionUtils.isNotEmpty(members)) {
            redisTemplate.opsForSet().add(key, members.toArray(new Object[0]));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<ZSetOperations.TypedTuple<T>> getSetWithScore(final String key) {
        final Set items = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        return Optional.ofNullable(items).map(s -> (Set<ZSetOperations.TypedTuple<T>>) s).orElse(Collections.emptySet());
    }

    public static <T> void addSetWithScore(final String key, final T value, final double score) {
        final Set items = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 添加到队列
     */
    public static <T> void addQueue(final String key, final Collection<T> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            redisTemplate.opsForList().leftPushAll(key, values.toArray());
        }
    }

    /**
     * 获取并删除一个队列元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T pollQueue(final String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 查看队列大小
     */
    public static long sizeQueue(final String key) {
        return Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
    }

    /**
     * 添加元素到延时队列
     */
    public static <T> void addToDelayQueue(final String key, final Collection<T> values, final long timeout, final TimeUnit unit) {
        if (CollectionUtils.isNotEmpty(values)) {
            final double expireTimeMillis = (double) (System.currentTimeMillis() + TimeoutUtils.toMillis(timeout, unit));
            redisTemplate.opsForZSet().add(key, values.stream().map(v -> new DefaultTypedTuple<Object>(v, expireTimeMillis)).collect(Collectors.toSet()));
        }
    }

    /**
     * 获取并删除一个延时队列元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T pollFromDelayQueue(final String key) {
        final double max = System.currentTimeMillis();
        final Set<T> items = (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, -1, max, 0, 1);
        final T value = Optional.ofNullable(items).orElse(Collections.emptySet()).stream().findFirst().orElse(null);
        if (Objects.nonNull(value)) {
            redisTemplate.opsForZSet().remove(key, value);
        }
        return value;
    }

    /**
     * 获取并删除一个延时队列元素
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<ZSetOperations.TypedTuple<T>> pollAllFromDelayQueue(final String key) {
        final double max = System.currentTimeMillis();
        final Set items = redisTemplate.opsForZSet().rangeByScoreWithScores(key, -1, max, 0, Long.MAX_VALUE);
        redisTemplate.opsForZSet().removeRangeByScore(key, -1, max);
        return Optional.ofNullable(items).map(s -> (Set<ZSetOperations.TypedTuple<T>>) s).orElse(Collections.emptySet());
    }

    /**
     * 执行自定义脚本
     */
    public static <T> T execute(final RedisScript<T> script, final List<String> keys, final Object... args) {
        return redisTemplate.execute(script, keys, args);
    }
}
