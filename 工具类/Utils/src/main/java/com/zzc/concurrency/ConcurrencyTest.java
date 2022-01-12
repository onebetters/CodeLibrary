package com.zzc.concurrency;

import com.zzc.redisson.RedisLockUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 * <p>Filename: com.zzc.concurrency.ConcurrencyTest.java</p>
 * <p>Date: 2022-01-12 15:48.</p>
 *
 * @author zhichuanzhang
 * @version 1.0.0
 */
@Component
@Slf4j
public class ConcurrencyTest {

    @Resource
    private RedisLockUtil redisLockUtil;

    /**
     * 请求总数
     */
    private static final int CLIENT_TOTAL = 10000;

    /**
     * 同时并发执行的线程数
     */
    private static final int THREAD_TOTAL = 100;

    /**
     * 由于是多线程并行操作count，因此要使用原子操作
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    @SneakyThrows
    public void unlockTest() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 信号量，用户控制并发线程数
        final Semaphore semaphore = new Semaphore(THREAD_TOTAL);
        // 闭锁，实现计数器递减
        final CountDownLatch countDownLatch = new CountDownLatch(CLIENT_TOTAL);
        for (int i = 0; i < CLIENT_TOTAL; i++){
            executorService.execute(()->{
                try {
                    // 获取执行许可，当许可不超过200时，允许通过，否则线程阻塞等待，直到获取许可
                    semaphore.acquire();
                    testUnlock("TP66666666666666");
                    // 释放许可
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 闭锁减一
                countDownLatch.countDown();
            });
        }
        // main线程阻塞等待，知道闭锁值为0才继续向下执行
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("count = " + count);
    }

    private void testUnlock(String tid) {
        count.incrementAndGet();
        RLock rLock1 = redisLockUtil.fairLock(tid);
        try {
            //加锁
            RLock rLock2 = redisLockUtil.fairLock(tid);
            rLock2.lock();
            //执行
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {
                log.error("休眠出错了", e);
            } finally {
                log.info("rLock2:" + rLock2 + ",interrupted:" + Thread.currentThread().isInterrupted()
                                 + ",hold:" + rLock2.isHeldByCurrentThread() + ",threadId:" + Thread.currentThread().getId());

                //解锁
                rLock2.unlock();
            }
        } finally {
            //解锁
            log.info("rLock1:" + rLock1+ ",interrupted:" + Thread.currentThread().isInterrupted()
                             + ",hold:" + rLock1.isHeldByCurrentThread() + ",threadId:" + Thread.currentThread().getId());

            rLock1.unlock();
        }
    }
}
