package org.pixonic;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Планировщик выполняющий задачи в указанное время
 */
public interface ScheduledExecutor {

    /**
     * Регистрируем задачу для выполнения в указанное время
     * @param date Время выполнения задачи
     * @param callable Код задачи
     */
    void schedule(Date date, Callable<?> callable);

}
