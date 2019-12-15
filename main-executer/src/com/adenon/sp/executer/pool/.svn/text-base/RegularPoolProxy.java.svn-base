package com.adenon.sp.executer.pool;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.executer.config.ProxyConfiguration;
import com.adenon.sp.executer.config.TaskRuntimeInfo;
import com.adenon.sp.executer.task.RegularTask;
import com.adenon.sp.executer.task.pool.IThreadPool;


public class RegularPoolProxy<P extends IThreadPoolInternal> implements IThreadPool {

    protected P                            pool;
    protected ProxyConfiguration           proxyConfig;
    //
    private Map<Class<?>, TaskRuntimeInfo> taskMap = new ConcurrentHashMap<Class<?>, TaskRuntimeInfo>();
    protected IAdministrationService       configuration;

    public RegularPoolProxy(P pool,
                            String bundleName,
                            IAdministrationService configuration) {
        this.configuration = configuration;
        this.proxyConfig = new ProxyConfiguration(bundleName, pool.getConfiguration().getPoolName());
        try {
            this.proxyConfig.setPoolConf(pool.getConfiguration());
            configuration.registerBean(this.proxyConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.pool = pool;
    }

    // Execution

    @Override
    public void execute(Runnable task) {
        this.submit(task, null);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return this.submit(task, null);
    }

    @Override
    public <T> Future<T> submit(Runnable task,
                                T result) {
        RegularTask<T> rtask = this.newTaskFor(task, result);
        Future<T> future = this.pool.executeTask(rtask);
        return new TFuture<T>(future, rtask.getTaskInfo());
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        RegularTask<T> rtask = this.newTaskFor(task);
        Future<T> future = this.pool.executeTask(rtask);
        return new TFuture<T>(future, rtask.getTaskInfo());
    }

    private <T> RegularTask<T> newTaskFor(Callable<T> callable) {
        TaskRuntimeInfo runtimeInfo = this.getRuntimeInfo(callable.getClass());
        RegularTask<T> rTask = new RegularTask<T>(callable, this.proxyConfig, runtimeInfo);
        return rTask;
    }

    private <T> RegularTask<T> newTaskFor(Runnable runnable,
                                          T value) {
        TaskRuntimeInfo runtimeInfo = this.getRuntimeInfo(runnable.getClass());
        RegularTask<T> rtask = new RegularTask<T>(runnable, value, this.proxyConfig, runtimeInfo);
        return rtask;
    };

    private <T> TaskRuntimeInfo getRuntimeInfo(Class<?> taskClass) {
        TaskRuntimeInfo runtimeInfo = this.taskMap.get(taskClass);
        if (runtimeInfo == null) {
            synchronized (taskClass) {
                if ((runtimeInfo = this.taskMap.get(taskClass)) == null) {
                    runtimeInfo = new TaskRuntimeInfo(taskClass.getSimpleName(), this);
                    try {
                        this.configuration.registerBean(runtimeInfo);
                    } catch (Exception e) {
                        new RuntimeException(e);
                    }
                    this.taskMap.put(taskClass, runtimeInfo);
                }
            }
        }
        return runtimeInfo;
    }

    //
    public String getBundleName() {
        return this.getConfig().getBundleName();
    }

    @Override
    public int getCorePoolSize() {
        return this.pool.getCorePoolSize();
    }

    @Override
    public int getMaximumPoolSize() {
        return this.pool.getMaximumPoolSize();
    }

    @Override
    public long getKeepAliveTime(TimeUnit unit) {
        return this.pool.getKeepAliveTime(unit);
    }

    @Override
    public int getPoolSize() {
        return this.pool.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return this.pool.getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return this.pool.getLargestPoolSize();
    }

    @Override
    public long getTaskCount() {
        return this.pool.getTaskCount();
    }

    @Override
    public long getCompletedTaskCount() {
        return this.pool.getCompletedTaskCount();
    }

    public ProxyConfiguration getConfig() {
        return this.proxyConfig;
    }

    @Override
    public String getPoolName() {
        return this.pool.getPoolName();
    }

    @Override
    public String toString() {
        return "[Proxy# " + super.toString() + "]";
    }

}
