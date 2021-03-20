package com.example;

import com.example.aggregator.Aggregator;
import com.example.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Executors 返回线程池对象的弊端如下：
 *
 * FixedThreadPool 和 SingleThreadExecutor ： 允许请求的队列长度为 Integer.MAX_VALUE ，可能堆积大量的请求，从而导致 OOM。
 * CachedThreadPool 和 ScheduledThreadPool ： 允许创建的线程数量为 Integer.MAX_VALUE ，可能会创建大量线程，从而导致 OOM。
 * @author Administrator
 */
@Slf4j
@Component
public class AggEventBus {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    //private final Executor     executor = Executors.newFixedThreadPool(10);
    //《阿里巴巴 Java 开发手册》中强制线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，
    // 这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险
    private final Executor     executor;
    private final Aggregator[] aggregators;
    private final boolean      async;

    @Autowired
    public AggEventBus(final ApplicationContext applicationContext, @Value("${aggregators.async:true}") final Boolean async) {
        final String[] names = applicationContext.getBeanNamesForType(Aggregator.class);
        this.aggregators = Stream.of(names).map(name -> applicationContext.getBean(name, Aggregator.class)).toArray(value -> new Aggregator[names.length]);
        log.info("Finish aggregators load，size={}, names={}", names.length, names);

        this.executor = this.initExecutor();
        this.async = Optional.ofNullable(async).orElse(false);
    }

    private Executor initExecutor() {
        final SecurityManager s = System.getSecurityManager();
        final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        int processors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                processors * 5,
                processors * 10,
                180L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                r -> new Thread(group, r, "MetadataEvents-" + threadNumber.getAndIncrement(), 0),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public <T extends Event> void send(final T source) {
        Arrays.stream(aggregators).filter(aggregator -> aggregator.support(source)).forEach(aggregator -> this.aggregate(aggregator, source));
    }

    private <T extends Event> void aggregate(final Aggregator aggregator, final T source) {
        if (async) {
            executor.execute(() -> aggregator.aggregate(source));
        } else {
            aggregator.aggregate(source);
        }
    }

    public void aggregateAll() {
        Arrays.stream(aggregators).forEach(aggregator -> aggregator.aggregate(LocalDateTime.now()));
    }
}
