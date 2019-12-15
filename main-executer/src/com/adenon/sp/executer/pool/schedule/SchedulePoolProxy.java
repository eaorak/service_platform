package com.adenon.sp.executer.pool.schedule;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.executer.config.ScheduleTaskRuntimeInfo;
import com.adenon.sp.executer.pool.RegularPoolProxy;
import com.adenon.sp.executer.task.SchType;
import com.adenon.sp.executer.task.ScheduleTask;
import com.adenon.sp.executer.task.pool.IScheduledPool;


public class SchedulePoolProxy extends RegularPoolProxy<IScheduledPoolInternal> implements IScheduledPool {

    public SchedulePoolProxy(IScheduledPoolInternal pool,
                             String bundleName,
                             IAdministrationService configuration) {
        super(pool, bundleName, configuration);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay,
                                       TimeUnit unit) {
        ScheduleTaskRuntimeInfo taskInfo = ScheduleTaskRuntimeInfo.create(command, this, SchType.SCHEDULE).setDelay(delay).setUnit(unit);
        ScheduleTask<?> task = this.createTask(command, taskInfo);
        return this.schedule(task);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay,
                                           TimeUnit unit) {
        ScheduleTaskRuntimeInfo taskInfo = ScheduleTaskRuntimeInfo.create(callable, this, SchType.SCHEDULE).setDelay(delay).setUnit(unit);
        ScheduleTask<V> task = this.createTask(callable, taskInfo);
        return this.schedule(task);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        ScheduleTaskRuntimeInfo taskInfo = ScheduleTaskRuntimeInfo.create(command, this, SchType.FIXED_RATE)
                                                                  .setInitialDelay(initialDelay)
                                                                  .setPeriod(period)
                                                                  .setUnit(unit);
        ScheduleTask<?> task = this.createTask(command, taskInfo);
        return this.schedule(task);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit) {
        ScheduleTaskRuntimeInfo taskInfo = ScheduleTaskRuntimeInfo.create(command, this, SchType.FIXED_DELAY)
                                                                  .setInitialDelay(initialDelay)
                                                                  .setDelay(delay)
                                                                  .setUnit(unit);
        ScheduleTask<?> task = this.createTask(command, taskInfo);
        return this.schedule(task);
    }

    public <V> ScheduledFuture<V> schedule(ScheduleTask<V> task) {
        ScheduledFuture<V> schedule = this.pool.schedule(task);
        return task.injectFuture(schedule);
    }

    private ScheduleTask<?> createTask(Runnable command,
                                       ScheduleTaskRuntimeInfo taskInfo) {
        ScheduleTask<?> task = new ScheduleTask<Void>(command, null, this.proxyConfig, taskInfo);
        try {
            this.configuration.registerBean(taskInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    private <V> ScheduleTask<V> createTask(Callable<V> command,
                                           ScheduleTaskRuntimeInfo taskInfo) {
        ScheduleTask<V> task = new ScheduleTask<V>(command, this.proxyConfig, taskInfo);
        try {
            this.configuration.registerBean(taskInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
