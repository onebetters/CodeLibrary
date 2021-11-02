package com.zzc.redis.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
public class FastjsonSerializer implements Serializer {

    private final static ParserConfig parserConfig = new ParserConfig();

    static {
        parserConfig.setAutoTypeSupport(true);
        parserConfig.addAccept("com.");
    }

    @Override
    public String serialize(@Nonnull Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteClassName, SerializerFeature.SortField);
    }

    @Override
    public <T> T deserialize(@Nonnull String valueFromCache, @Nonnull Class<T> requiredClass) {
        return JSON.parseObject(valueFromCache, requiredClass, parserConfig);
    }
}
