package org.pixonic;

import java.util.concurrent.ExecutorService;

public class ScheduledPump extends Thread {

    private static final int SHUTDOWN_PERIOD_MS = 60000;

    private final ScheduledQueue queue;

    private final ExecutorService executor;

    public ScheduledPump(ScheduledQueue queue, ExecutorService executor) {
        this.queue = queue;
        this.executor = executor;

        this.setName("Pump thread");
        this.setDaemon(false);
        this.setPriority(Thread.NORM_PRIORITY);
    }

    public void shutdown() throws InterruptedException {
        interrupt();

        join(SHUTDOWN_PERIOD_MS);

        if (isAlive()) {
            System.err.println("Pump thread is not interrupred");
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            ScheduledEntry entry;
            try {
                entry = queue.take();
            } catch (InterruptedException e) {
                return;
            }

            executor.submit(entry.getCallable());
        }
    }
}
