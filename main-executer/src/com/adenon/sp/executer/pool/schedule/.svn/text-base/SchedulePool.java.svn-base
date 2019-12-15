package com.adenon.sp.executer.pool.schedule;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.executer.config.PoolConfiguration;
import com.adenon.sp.executer.config.ScheduleTaskRuntimeInfo;
import com.adenon.sp.executer.pool.PoolUtil;
import com.adenon.sp.executer.pool.RegularPoolProxy;
import com.adenon.sp.executer.task.RegularTask;
import com.adenon.sp.executer.task.ScheduleTask;


public class SchedulePool extends ScheduledThreadPoolExecutor implements IScheduledPoolInternal {

    private PoolConfiguration                poolConfig;
    private PoolUtil<IScheduledPoolInternal> util;

    public static SchedulePool createPool(PoolConfiguration config) {
        config.validate();
        SchedulePool scheduledPool = new SchedulePool(config);
        return scheduledPool;
    }

    @Override
    public PoolUtil<IScheduledPoolInternal> util() {
        return this.util;
    }

    private SchedulePool(PoolConfiguration config) {
        super(config.getCoreSize(), config.getFactory(), config.getRejectHandler());
        this.poolConfig = config;
        this.util = new PoolUtil<IScheduledPoolInternal>(this);
        //
        config.injectPool(this);
        config.getFactory().injectPool(this);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay,
                                       TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ScheduledFuture<T> schedule(ScheduleTask<T> task) {
        ScheduleTaskRuntimeInfo info = (ScheduleTaskRuntimeInfo) task.getTaskInfo();
        ScheduledFuture<?> schedule = null;
        switch (info.getScheduleType()) {
            case SCHEDULE:
                schedule = super.schedule((Runnable) task, info.getDelay(), info.getUnit());
                break;
            case FIXED_RATE:
                schedule = super.scheduleAtFixedRate(task, info.getInitialDelay(), info.getPeriod(), info.getUnit());
                break;
            case FIXED_DELAY:
                schedule = super.scheduleWithFixedDelay(task, info.getInitialDelay(), info.getDelay(), info.getUnit());
                break;
            default:
                throw new RuntimeException("Invalid schedule type : " + info.getType() + " !");
        }
        return (ScheduledFuture<T>) schedule;
    }

    @Override
    public PoolConfiguration getConfiguration() {
        return this.poolConfig;
    }

    @Override
    public RegularPoolProxy<?> createProxy(String bundleName) {
        return new SchedulePoolProxy(this, bundleName, this.poolConfig.getConfiguration());
    }

    @Override
    public boolean addNewThread(int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> executeTask(RegularTask<T> task) {
        super.execute(task);
        return task;
    }

    @Override
    public int getQueueSize() {
        return this.getQueue().size();
    }

    @Override
    public String getQueueGraph() {
        return "This operation is not supported for scheduled pool.";
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> runnables = super.shutdownNow();
        return runnables;
    }

    @Override
    public String getPoolName() {
        return this.poolConfig.getPoolName();
    }

}
