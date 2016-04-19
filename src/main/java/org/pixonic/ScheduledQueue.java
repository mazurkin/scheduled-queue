package org.pixonic;

import java.util.PriorityQueue;

/**
 * Нам нужен потокобезопасный блокирующий min-heap. В java есть готовая реализация кучи - это PriorityQueue.
 * Блокирующее чтение и потокобезопасность обеспечим вручную.
 * Компаратор класса Entry обеспечит то, что эта очередь будет именно min-heap
 */
public class ScheduledQueue {

    // Если разница между текущим и планируемым временем окажется меньше этой величины, то мы выполним задание,
    // поскольку особого смысла ожидать такое малое количество времени нет.
    private static final long PRECISION_MS = 50;

    private final PriorityQueue<ScheduledEntry> queue;

    private final Object monitor;

    public ScheduledQueue() {
        this.queue = new PriorityQueue<>();
        this.monitor = new Object();
    }

    public void put(ScheduledEntry item) {
        synchronized (monitor) {
            queue.add(item);
            monitor.notifyAll();
        }
    }

    public ScheduledEntry take() throws InterruptedException {
        for (;;) {
            synchronized (monitor) {
                ScheduledEntry item = queue.peek();
                if (item != null) {
                    long now = System.currentTimeMillis();
                    long delay = item.getMoment() - now;

                    if (delay > PRECISION_MS) {
                        // Ожидаем текущий элемент пока он не созреет, но в тоже время мы всегда готовы прерваться
                        // для анализа нового элемента, если вновь поступивший элемент окажется еще лучше чем текущий
                        monitor.wait(delay);
                    } else {
                        return queue.poll();
                    }
                } else {
                    monitor.wait();
                }
            }
        }
    }
}
