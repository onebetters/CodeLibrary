package threadpoll.limited;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.Test;

import java.lang.instrument.Instrumentation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * TODO
 * <p>Filename: threadpoll.limited.MemoryLimitedLinkedBlockingQueueTest.java</p>
 * <p>Date: 2022-07-05 17:51.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
public class MemoryLimitedLinkedBlockingQueueTest {

    @Test
    public void test() throws Exception {
        ByteBuddyAgent.install();
        final Instrumentation instrumentation = ByteBuddyAgent.getInstrumentation();
        MemoryLimitedLinkedBlockingQueue<Object> queue = new MemoryLimitedLinkedBlockingQueue<>(1, instrumentation);

        //an object needs more than 1 byte of space, so it will fail here
        assertThat(queue.offer(new Object()), is(false));

        //will success
        queue.setMemoryLimit(Integer.MAX_VALUE);
        assertThat(queue.offer(new Object()), is(true));
    }
}
