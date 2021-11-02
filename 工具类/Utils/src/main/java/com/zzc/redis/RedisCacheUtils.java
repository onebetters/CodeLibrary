package com.zzc.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import com.zzc.redis.cache.Serializer;
import com.zzc.redis.cache.Serializers;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class RedisCacheUtils {
    private final static String KEY_PREFIX = "cache:v1:";
    private final static String DEFAULT_NULL_CACHE = "";
    private final static long DEFAULT_NULL_EXPIRE_SECONDS = TimeoutUtils.toSeconds(10, TimeUnit.MINUTES);
    private final static Predicate<String> DEFAULT_PREDICATE = s -> StringUtils.equals(DEFAULT_NULL_CACHE, s);

    // public static <T> T get(@Nonnull final String key, @Nonnull final Class<T> clazz, @Nonnull final Supplier<T> supplier) {
    //     return get(key, clazz, supplier, -1, TimeUnit.MILLISECONDS);
    // }

    public static <T> T get(@Nonnull final String key,
            @Nonnull final Class<T> clazz,
            @Nonnull final Supplier<T> supplier,
            final long timeout,
            @Nonnull final TimeUnit unit) throws SerializationException {
        final Serializer serializer = Serializers.useJackson();
        return get(key, clazz, supplier, timeout, unit, serializer);
    }

    public static <T> T get(@Nonnull final String key,
            @Nonnull final Class<T> clazz,
            @Nonnull final Supplier<T> supplier,
            final long timeout,
            @Nonnull final TimeUnit unit,
            @Nonnull final Serializer serializer) throws SerializationException {
        final String keyUse = KEY_PREFIX + key;
        final String redisCache = RedisUtils.get(keyUse);
        if (StringUtils.isNotBlank(redisCache)) {
            // NULL值校验
            // 版本号过期校验 待实现
            if (DEFAULT_PREDICATE.test(redisCache)) {
                return null;
            } else {
                return serializer.deserialize(redisCache, clazz);
            }
        } else {
            final T supplierValue = supplier.get();
            if (Objects.isNull(supplierValue) || (supplierValue instanceof CharSequence && StringUtils.isBlank((CharSequence) supplierValue))) {
                final long timeoutSeconds = timeout > 0 ? TimeoutUtils.toSeconds(timeout, unit) : DEFAULT_NULL_EXPIRE_SECONDS;
                RedisUtils.set(keyUse, DEFAULT_NULL_CACHE, timeoutSeconds);
            } else {
                final String cached = serializer.serialize(supplierValue);
                if (timeout <= 0) {
                    RedisUtils.set(keyUse, cached);
                } else {
                    RedisUtils.set(keyUse, cached, timeout, unit);
                }
            }
            return supplierValue;
        }
    }
}
