package com.adenon.sp.executer.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

import com.adenon.sp.executer.config.ProxyConfiguration;
import com.adenon.sp.executer.config.ScheduleTaskRuntimeInfo;
import com.adenon.sp.executer.config.TaskRuntimeInfo;
import com.adenon.sp.executer.pool.helper.SpThread;
import com.adenon.sp.executer.task.pool.IManagedInternal;


public class ScheduleTask<T> implements Callable<T>, Runnable, IManagedInternal {

    private ProxyConfiguration      proxyConfig;
    private ScheduleTaskRuntimeInfo runtimeInfo;
    private long                    start;
    private volatile boolean        running = false;
    private String                  taskClass;
    private Callable<T>             callable;

    public ScheduleTask(Callable<T> callable,
                        ProxyConfiguration config,
                        ScheduleTaskRuntimeInfo runtimeInfo) {
        this.callable = callable;
        this.init(config, runtimeInfo, callable);
    }

    public ScheduleTask(Runnable runnable,
                        T result,
                        ProxyConfiguration config,
                        ScheduleTaskRuntimeInfo runtimeInfo) {
        this.callable = new Call(runnable, result);
        this.init(config, runtimeInfo, runnable);
    }

    private void init(ProxyConfiguration config,
                      ScheduleTaskRuntimeInfo runtimeInfo,
                      Object runee) {
        this.proxyConfig = config;
        this.runtimeInfo = runtimeInfo;
        this.taskClass = runee.getClass().getName();
    }

    private class Call implements Callable<T> {

        Runnable r;
        T        result;

        public Call(Runnable r,
                    T result) {
            this.r = r;
            this.result = result;
        }

        @Override
        public T call() throws Exception {
            this.r.run();
            return this.result;
        }
    }

    @Override
    public T call() throws Exception {
        T result = null;
        boolean success = false;
        try {
            this.running();
            result = this.callable.call();
            success = true;
        } finally {
            this.runtimeInfo.increaseRun(this.getDelta(), success);
            this.stopping();
        }
        return result;
    }

    @Override
    public void run() {
        try {
            this.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void running() {
        this.running = true;
        this.start = System.currentTimeMillis();
        ((SpThread) Thread.currentThread()).setCurrentTask(this);
    }

    private void stopping() {
        this.running = false;
        this.start = 0;
        ((SpThread) Thread.currentThread()).setCurrentTask(null);
    }

    @Override
    public long getDelta() {
        return this.start == 0 ? 0 : System.currentTimeMillis() - this.start;
    }

    public int getPriority() {
        return this.runtimeInfo.getPriority();
    }

    public String getBundleName() {
        return this.proxyConfig.getBundleName();
    }

    public boolean isRunning() {
        return this.running;
    }

    @Override
    public String getTaskClass() {
        return this.taskClass;
    }

    public TaskRuntimeInfo getTaskInfo() {
        return this.runtimeInfo;
    }

    public <V> ScheduledFuture<V> injectFuture(ScheduledFuture<V> schedule) {
        return this.runtimeInfo.injectFuture(schedule, this);
    }

    @Override
    public long getMaxExecutionTime() {
        return 0;
    }

    @Override
    public void interrupting() {
        //
    }

    //
    @Override
    public String toString() {
        return "Task: " + this.taskClass;
    }

}
