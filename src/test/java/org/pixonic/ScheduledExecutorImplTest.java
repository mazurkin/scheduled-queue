package org.pixonic;

import java.util.Date;

import org.junit.After;
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

        // Так что пока проверим работу наглядно

        long now = System.currentTimeMillis();

        scheduledExecutor.schedule(new Date(now + 4000), () -> {
            System.out.println("4001");
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 3000), () -> {
            System.out.println("3000");
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 1000), () -> {
            System.out.println("1000");
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 2000), () -> {
            System.out.println("2000");
            return null;
        });

        scheduledExecutor.schedule(new Date(now + 4000), () -> {
            System.out.println("4002");
            return null;
        });

        Thread.sleep(10000);

        System.out.println("Trying to shutdown");
    }
}