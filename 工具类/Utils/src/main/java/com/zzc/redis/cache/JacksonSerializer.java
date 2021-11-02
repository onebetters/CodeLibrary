package com.zzc.redis.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.data.redis.serializer.SerializationException;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;

/**
 * @author Administrator
 */
public class JacksonSerializer implements Serializer {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new ParameterNamesModule()).registerModule(new JavaTimeModule()).registerModule(new Jdk8Module());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Override
    public String serialize(@Nonnull Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(@Nonnull String valueFromCache, @Nonnull Class<T> requiredClass) {
        try {
            return objectMapper.readValue(valueFromCache, requiredClass);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize: " + e.getMessage(), e);
        }
    }
}
