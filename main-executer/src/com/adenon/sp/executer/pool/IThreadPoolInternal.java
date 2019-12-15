package com.adenon.sp.executer.pool;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.executer.config.PoolConfiguration;
import com.adenon.sp.executer.task.RegularTask;
import com.adenon.sp.executer.task.pool.IThreadPoolCore;


public interface IThreadPoolInternal extends IThreadPoolCore {

    RegularPoolProxy<?> createProxy(String bundleName);

    <T> Future<T> executeTask(RegularTask<T> task);

    @Override
    PoolConfiguration getConfiguration();

    @Override
    int getPoolSize();

    String getQueueGraph();

    void setKeepAliveTime(long time,
                          TimeUnit unit);

    void shutdown();

    List<Runnable> shutdownNow();

    PoolUtil<?> util();

}
