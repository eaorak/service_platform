package com.adenon.sp.executer;

import com.adenon.sp.executer.task.cron.ICronPool;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.executer.task.pool.IThreadPool;

public interface IExecuterManager {

    /**
     * Get the default thread pool of system.
     * 
     * @param bundleName Bundle that requires the pool.
     * @return System thread pool
     */
    IThreadPool getDefaultPool(String bundleName);

    /**
     * Get the default schedule pool of system.
     * 
     * @param bundleName Bundle that requires the pool.
     * @return Schedule thread pool
     */
    IScheduledPool getScheduledPool(String bundleName);

    /**
     * Get the default cron schedule pool of system which <br>
     * can be used for scheduling cron like jobs.
     * 
     * @param bundleName Bundle that requires the pool.
     * @return Cron schedule pool
     */
    ICronPool getCronPool(String bundleName);

    //

    /**
     * Create new pool configuration.
     */
    IPoolConfig newConfig(String bundleName,
                          String poolName);

    /**
     * Creates new thread pool with given configuration.
     * 
     * @param configuration Pool configuration.
     * @return New pool
     * @throws Exception
     */
    IThreadPool createPool(IPoolConfig configuration) throws Exception;

    /**
     * Creates new/gets existing thread pool with given configuration/name.
     * 
     * @param configuration Pool configuration.
     * @return New or existing pool
     * @throws Exception
     */
    IThreadPool createOrGetPool(IPoolConfig configuration) throws Exception;

    /**
     * Gets existing thread pool with name.
     * 
     * @param poolName Pool name
     * @param bundleName Bundle that requires the pool.
     * @return Thread pool
     */
    IThreadPool getPool(String poolName,
                        String bundleName);

    /**
     * Deletes existing thread pool with name, if no tasks exist on it.
     * 
     * @param poolName
     * @return success or failure
     */
    boolean deletePool(String poolName);

}
