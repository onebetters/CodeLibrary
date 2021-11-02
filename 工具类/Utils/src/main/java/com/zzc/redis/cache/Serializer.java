package com.zzc.redis.cache;

import org.springframework.data.redis.serializer.SerializationException;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
public interface Serializer {

    String serialize(@Nonnull final Object object) throws SerializationException;

    <T> T deserialize(@Nonnull final String valueFromCache, @Nonnull final Class<T> requiredClass) throws
            SerializationException;
}
