package com.zzc.annotation.limitApi;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口限制访问次数
 *
 * 使用注意事项：
 *  1、限制是根据请求接口的全部参数来判断的，如果接口参数发生变化，拦截不生效
 *  2、如要想限制同一个人不同参数调用接口，因根据IP、user信息改造
 *
 * @author Administrator
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LimitApi {
    /**
     * 限流唯一标示 （可以为空，为空时会以接口参数作为key）
     * 如果需要限制一个接口调用N次，可以指定key
     */
    String key() default "";

    /**
     * expire时间内限制访问的次数
     */
    long frequency() default 1;

    /**
     * 过期时间，默认10秒
     */
    long expire() default 10;

    /**
     * 过期时间类型，默认"秒"
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}
