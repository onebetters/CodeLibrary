package threadpoll.safe;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.Test;

import java.lang.instrument.Instrumentation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 测试
 * <p>Filename: threadpoll.safe.MemorySafeLinkedBlockingQueueTest.java</p>
 * <p>Date: 2022-07-05 19:37.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
public class MemorySafeLinkedBlockingQueueTest {
    @Test
    public void test() throws Exception {
        ByteBuddyAgent.install();
        final Instrumentation instrumentation = ByteBuddyAgent.getInstrumentation();
        final long objectSize = instrumentation.getObjectSize((Runnable) () -> {
        });
        // 获取当前可用内存
        int maxFreeMemory = (int) MemoryLimitCalculator.maxAvailable();
        // 初始化队列，并且直接指定maxFreeMemory，这样这个队列就直接不能接受元素了
        MemorySafeLinkedBlockingQueue<Runnable> queue = new MemorySafeLinkedBlockingQueue<>(maxFreeMemory);
        // all memory is reserved for JVM, so it will fail here
        // 当前情况下，队列不能放东西了，所以返回false
        assertThat(queue.offer(() -> {
        }), is(false));

        // maxFreeMemory-objectSize Byte memory is reserved for the JVM, so this will succeed
        // 重置maxFreeMemory后，可以放入元素
        queue.setMaxFreeMemory((int) (MemoryLimitCalculator.maxAvailable() - objectSize));
        assertThat(queue.offer(() -> {
        }), is(true));
    }
}
