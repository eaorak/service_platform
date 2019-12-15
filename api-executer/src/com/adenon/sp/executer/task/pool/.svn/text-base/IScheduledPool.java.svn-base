package com.adenon.sp.executer.task.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled pool.
 */
public interface IScheduledPool extends IThreadPool {

    /**
     * See {@link java.util.concurrent.ScheduledExecutorService#schedule} for detailed explanation. <br>
     * ALSO SEE {@link com.adenon.sp.executer.task.TaskId} for unification of task id values.
     */
    ScheduledFuture<?> schedule(Runnable task,
                                long delay,
                                TimeUnit unit);

    /**
     * See {@link java.util.concurrent.ScheduledExecutorService#schedule} for detailed explanation. <br>
     * ALSO SEE {@link com.adenon.sp.executer.task.TaskId} for unification of task id values.
     */
    <V> ScheduledFuture<V> schedule(Callable<V> task,
                                    long delay,
                                    TimeUnit unit);

    /**
     * See {@link java.util.concurrent.ScheduledExecutorService#scheduleAtFixedRate} for detailed explanation. <br>
     * ALSO SEE {@link com.adenon.sp.executer.task.TaskId} for unification of task id values.
     */
    ScheduledFuture<?> scheduleAtFixedRate(Runnable task,
                                           long initialDelay,
                                           long period,
                                           TimeUnit unit);

    /**
     * See {@link java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay} for detailed explanation. <br>
     * ALSO SEE {@link com.adenon.sp.executer.task.TaskId} for unification of task id values.
     */
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable task,
                                              long initialDelay,
                                              long delay,
                                              TimeUnit unit);

}
