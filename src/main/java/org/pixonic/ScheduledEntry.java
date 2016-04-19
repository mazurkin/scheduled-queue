package org.pixonic;

import java.util.concurrent.Callable;

public final class ScheduledEntry {

    private final long moment;

    private final Callable<?> callable;

    private final long index;

    public ScheduledEntry(long moment, Callable<?> callable, long index) {
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

    public long getIndex() {
        return index;
    }

}
