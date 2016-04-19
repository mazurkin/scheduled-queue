package org.pixonic;

import org.junit.Assert;
import org.junit.Test;


public class ScheduledQueueTest {

    @Test
    public void test() throws Exception {
        ScheduledQueue queue = new ScheduledQueue();
        queue.put(new ScheduledEntry(4000, null, 3));
        queue.put(new ScheduledEntry(4000, null, 4));
        queue.put(new ScheduledEntry(3000, null, 2));
        queue.put(new ScheduledEntry(1000, null, 0));
        queue.put(new ScheduledEntry(2000, null, 1));

        Assert.assertEquals(0, queue.take().getIndex());
        Assert.assertEquals(1, queue.take().getIndex());
        Assert.assertEquals(2, queue.take().getIndex());
        Assert.assertEquals(3, queue.take().getIndex());
        Assert.assertEquals(4, queue.take().getIndex());
    }

}