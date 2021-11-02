package com.zzc.limitApi;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import com.zzc.utils.MD5Util;
import com.zzc.redis.RedisUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 接口访问限制
 * @author Administrator
 */
@Slf4j
@Aspect
@Component
public class LimitAspectSupport {

    private final static String KEY_PREFIX = "limit:v1:";

    private final static String SCRIPT_RATE_LIMIT =
    // @formatter:on
              "local key = KEYS[1]\n"
            + "local limitTotal = tonumber(ARGV[1]) \n"
            + "local ttlSeconds = tonumber(ARGV[2]) \n"
            + "local current = tonumber(redis.call('get', key) or 0)\n"
            + "if current + 1 > limitTotal then \n"
            + "  return 0\n"
            + "else \n"
            + "  redis.call('INCRBY', key, 1)\n"
            + "  redis.call('EXPIRE', key, ttlSeconds)\n"
            + "  return current + 1\n"
            + "end";
    // @formatter:off

    @Pointcut(value = "@annotation(limitApi)")
    public void limit(LimitApi limitApi) {
    }

    @Around(value = "limit(limitApi)", argNames = "joinPoint, limitApi")
    public Object aroundLog(final ProceedingJoinPoint joinPoint, final LimitApi limitApi) throws Throwable {
        final String key;
        if (StringUtils.isNotBlank(limitApi.key())) {
            key = KEY_PREFIX.concat(limitApi.key());
        } else {
            final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            final Object[] args = Optional.ofNullable(joinPoint.getArgs()).orElse(new Object[0]);
            //排除携带有Request或者Response对象，会导致序列化异常
            List<Object> logArgs = Arrays.stream(args)
                                         .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                                         .collect(Collectors.toList());
            key = KEY_PREFIX.concat(String.join(":",
                                                methodSignature.getDeclaringType().getSimpleName(),
                                                methodSignature.getName(),
                                                MD5Util.md5Hex(methodSignature.toString() + "-" + JSON.toJSONString(logArgs))));
        }

        Number count = RedisUtils.execute(new DefaultRedisScript<>(SCRIPT_RATE_LIMIT, Number.class),
                                          Collections.singletonList(key),
                                          limitApi.frequency(),
                                          limitApi.expire());
        if (count != null && count.intValue() != 0 && count.intValue() <= limitApi.frequency()) {
            log.debug("限流时间段内访问第：{} 次", count.toString());
            return joinPoint.proceed();
        }

        // throw new LimitException("操作太频繁了，赶紧休息会吧！");
        throw new LimitException(limitApi.expire() + "秒内只能操作" + limitApi.frequency() + "次，休息会吧，稍后再试！");
    }

}
