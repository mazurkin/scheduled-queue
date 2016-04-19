package org.pixonic;

import java.util.concurrent.Callable;

public final class ScheduledEntry implements Comparable<ScheduledEntry> {

    private final long moment;

    private final Callable<?> callable;

    /**
     * Поскольку в ТЗ сказано что мы должны сохранять порядок элементов с одинаковой датой - нам придется
     * ввести еще и искусственный монотонно возрастающий индекс элемента
     */
    private final int index;

    public ScheduledEntry(long moment, Callable<?> callable, int index) {
        this.moment = moment;
        this.callable = callable;
        this.index = index;
    }

    public long getMoment() {
        return moment;
    }

    public Callable<?> getCallable() {
        return callable;
    }

    public int getIndex() {
        return index;
    }

    public int compareTo(ScheduledEntry that) {
        int r = Long.compare(this.moment, that.moment);

        if (r == 0) {
            r = Integer.compare(this.index, that.index);
        }

        return r;
    }
}
