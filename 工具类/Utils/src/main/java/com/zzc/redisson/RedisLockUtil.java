package com.zzc.redisson;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * redis 加锁工具类
 * @author Administrator
 */
@Component
@Slf4j
public class RedisLockUtil {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 退货申请单前缀
     */
    public static final String EMAIL_SETTING_PREFIX = "ZZC_";

    public RLock fairLock(String s){
        RLock rlock = redissonClient.getFairLock(EMAIL_SETTING_PREFIX + s);
        log.error("client: {}", redissonClient);
        rlock.lock(10000, TimeUnit.MILLISECONDS);
        return rlock;
    }

    public Set<RLock> batchLock(Collection<String> keys){
        Set<RLock> rLocks = Sets.newHashSet();
        keys.forEach(s -> rLocks.add(fairLock(s)));
        return rLocks;
    }

    public void releaseRLock(Set<RLock> rLocks){
        rLocks.forEach(Lock::unlock);
    }
}
