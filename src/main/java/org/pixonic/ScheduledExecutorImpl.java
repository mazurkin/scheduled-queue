package org.pixonic;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledExecutorImpl implements ScheduledExecutor {

    private static final int NUM_CORE_THREADS = 1;

    private static final int NUM_MAX_THREADS = 10;

    private static final int SHUTDOWN_PERIOD_MS = 60000;

    private static final int QUEUE_CAPACITY = 1000;

    private final AtomicInteger sequence;

    private final ExecutorService executorService;

    private final ScheduledQueue queue;

    private final ScheduledPump pump;

    private volatile boolean shutdowned;

    public ScheduledExecutorImpl() {
        this.sequence = new AtomicInteger(0);

        this.executorService = new ThreadPoolExecutor(NUM_CORE_THREADS, NUM_MAX_THREADS, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY), new ThreadPoolExecutor.CallerRunsPolicy());

        this.queue = new ScheduledQueue();

        this.pump = new ScheduledPump(queue, executorService);
        this.pump.start();

        this.shutdowned = false;
    }

    public void shutdown() throws InterruptedException {
        this.shutdowned = true;

        this.pump.shutdown();

        this.executorService.shutdown();

        boolean succeed = this.executorService.awaitTermination(SHUTDOWN_PERIOD_MS, TimeUnit.MILLISECONDS);
        if (!succeed) {
            System.err.println("Fail to shutdown executor service");
        }
    }

    @Override
    public synchronized void schedule(Date date, Callable<?> callable) {
        if (shutdowned) {
            throw new IllegalStateException("Executor is shutting down");
        }

        ScheduledEntry entry = new ScheduledEntry(date.getTime(), callable,
                sequence.incrementAndGet());

        queue.put(entry);
    }

}
