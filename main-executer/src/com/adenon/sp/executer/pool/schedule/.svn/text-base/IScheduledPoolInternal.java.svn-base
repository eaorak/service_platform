package com.adenon.sp.executer.pool.schedule;

import java.util.concurrent.ScheduledFuture;

import com.adenon.sp.executer.pool.IThreadPoolInternal;
import com.adenon.sp.executer.task.ScheduleTask;
import com.adenon.sp.executer.task.pool.IScheduledPool;


public interface IScheduledPoolInternal extends IScheduledPool, IThreadPoolInternal {

    <T> ScheduledFuture<T> schedule(ScheduleTask<T> task);

}
