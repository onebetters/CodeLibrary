package com.zzc.redis.cache;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class Serializers {

    /**
     * 系列化、反系列化功能优缺点比对:
     * <p>
     * 优点: 常用的如lombok的@Value、@Builder注解的全参构造函数（不存在无参构造函数），默认支持（不考虑低版本fastjson）;
     * 缺点: 部分测试用例不通过，
     * . 如不存在无参构造函数时，只会通过有参函数初始化，不会反射多余的属性;
     * . 如多态org.apache.commons.collections4.keyvalue.DefaultKeyValue测试不通过，直接使用无参构造函数，丢失原本所有属性;
     */
    private static final Serializer FAST_JSON_SERIALIZER = new FastjsonSerializer();
    /**
     * 优化: 代码可能稳定点吧
     * 缺点: 没有无参构造函数时，且未指定如{@link com.fasterxml.jackson.annotation.JsonCreator}时，默认无法反系列化。
     * jdk8以上版本可通过挂载<code>jackson-modules-java8</code>及{@link com.fasterxml.jackson.module.paramnames.ParameterNamesModule}支持，
     * 且需要编译javac开启-parameters支持，详见: <pre>https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html</pre>
     */
    private static final Serializer JACKSON_SERIALIZER   = new JacksonSerializer();

    public static Serializer useFastJson() {
        return FAST_JSON_SERIALIZER;
    }

    public static Serializer useJackson() {
        return JACKSON_SERIALIZER;
    }
}
