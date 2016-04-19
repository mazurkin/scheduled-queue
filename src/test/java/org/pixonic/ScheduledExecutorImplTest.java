package org.pixonic;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScheduledExecutorImplTest {

    private ScheduledExecutorImpl scheduledExecutor;

    @Before
    public void setUp() throws Exception {
         scheduledExecutor = new ScheduledExecutorImpl();
    }

    @After
    public void tearDown() throws Exception {
        scheduledExecutor.shutdown();
    }

    @Test
    public void test() throws Exception {
        // Сюда надо бы добавить моков через EasyMock или Mockito, но в ТЗ сказано что можно использовать
        // любые встроенные средства Java8 - будет считать это ограничением

        List<Integer> result = new CopyOnWriteArrayList<>();

        long now = System.currentTimeMillis();

        scheduledExecutor.schedule(new Date(now + 4000), () -> {
            System.out.println("4001");
            result.add(4001);
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 3000), () -> {
            System.out.println("3000");
            result.add(3000);
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 1000), () -> {
            System.out.println("1000");
            result.add(1000);
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 2000), () -> {
            System.out.println("2000");
            result.add(2000);
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 4000), () -> {
            System.out.println("4002");
            result.add(4002);
            return null;
        });

        Thread.sleep(5000);

        Assert.assertEquals(1000, (int) result.get(0));
        Assert.assertEquals(2000, (int) result.get(1));
        Assert.assertEquals(3000, (int) result.get(2));
        Assert.assertEquals(4001, (int) result.get(3));
        Assert.assertEquals(4002, (int) result.get(4));
    }
}