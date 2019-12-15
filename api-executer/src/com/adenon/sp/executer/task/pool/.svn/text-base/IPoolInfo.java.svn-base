package com.adenon.sp.executer.task.pool;

import java.util.concurrent.TimeUnit;

public interface IPoolInfo {

    String getPoolName();

    int getCorePoolSize();

    int getMaximumPoolSize();

    long getKeepAliveTime(TimeUnit unit);

    /**
     * Returns the current number of threads in the pool.
     * 
     * @return the number of threads
     */
    int getPoolSize();

    /**
     * Returns the approximate number of threads that are actively executing tasks.
     * 
     * @return the number of threads
     */
    int getActiveCount();

    /**
     * Returns the largest number of threads that have ever simultaneously been in the pool.
     * 
     * @return the number of threads
     */
    int getLargestPoolSize();

    /**
     * Returns the approximate total number of tasks that have ever been scheduled for<br>
     * execution. Because the states of tasks and threads may change dynamically <br>
     * during computation, the returned value is only an approximation.
     * 
     * @return the number of tasks
     */
    long getTaskCount();

    long getCompletedTaskCount();

}
