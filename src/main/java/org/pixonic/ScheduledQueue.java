package org.pixonic;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Нам нужен потокобезопасный блокирующий min-heap. В java есть готовая реализация кучи - это PriorityQueue.
 *
 * Блокирующее чтение и потокобезопасность обеспечим вручную.
 *
 * Компаратор для класса Entry обеспечит то, что эта очередь будет именно min-heap
 */
public class ScheduledQueue {

    // Если разница между текущим и планируемым временем окажется меньше этой величины, то мы выполним задание,
    // поскольку особого смысла ожидать такое малое количество времени нет.
    private static final long PRECISION_MS = 10;

    // Внешний компаратор всегда дает больше свободы дальнейшего выбора чем имплементированный через интерфейс
    private static final Comparator<ScheduledEntry> COMPARATOR = (o1, o2) -> {
        int r = Long.compare(o1.getMoment(), o2.getMoment());
        if (r == 0) {
            r = Long.compare(o1.getIndex(), o2.getIndex());
        }
        return r;
    };

    private final PriorityQueue<ScheduledEntry> queue;

    private final Object monitor;

    public ScheduledQueue() {
        this.queue = new PriorityQueue<>(COMPARATOR);
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
                        // для анализа нового элемента - вдруг вновь поступивший элемент окажется еще лучше чем текущий
                        monitor.wait(delay);
                    } else {
                        // На самом деле мы можем вернуть другой элемент - не тот, который мы забрали в peek(), но даже
                        // если это так, то он будет еще более подходящим чем элемент из peek()
                        return queue.poll();
                    }
                } else {
                    monitor.wait();
                }
            }
        }
    }
}
