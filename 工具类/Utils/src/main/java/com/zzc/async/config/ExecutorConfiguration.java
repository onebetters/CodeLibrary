package com.zzc.async.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Administrator
 */
@Configuration
@ConditionalOnBean(annotation = {EnableAsync.class})
@EnableConfigurationProperties(AsyncProperties.class)
public class ExecutorConfiguration {
    @Bean("pwbExecutor")
    public AsyncTaskExecutor asyncTaskExecutor(AsyncProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        // 缓冲队列
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 允许线程的空闲时间60秒
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池对拒绝任务的处理策略,由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 子线程池，用于避免异步线程池嵌套导致的资源死锁
     */
    @Bean("subPwbExecutor")
    public AsyncTaskExecutor subAsyncTaskExecutor(AsyncProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        // 缓冲队列
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 允许线程的空闲时间60秒
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池对拒绝任务的处理策略,由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
