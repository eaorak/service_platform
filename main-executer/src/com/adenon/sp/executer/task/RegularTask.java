package com.adenon.sp.executer.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.config.ProxyConfiguration;
import com.adenon.sp.executer.config.TaskRuntimeInfo;
import com.adenon.sp.executer.pool.helper.SpThread;
import com.adenon.sp.executer.task.IManaged;
import com.adenon.sp.executer.task.pool.IManagedInternal;


public class RegularTask<T> extends FutureTask<T> implements Comparable<RegularTask<T>>, IManagedInternal {

    private static final Logger logger  = Logger.getLogger(RegularTask.class);
    private ProxyConfiguration  proxyConfig;
    private TaskRuntimeInfo     runtimeInfo;
    private long                start;
    private String              taskClass;
    private final Object        task;
    private volatile boolean    running = false;
    private boolean             managed = false;

    //
    public RegularTask(Callable<T> callable,
                       ProxyConfiguration config,
                       TaskRuntimeInfo runtimeInfo) {
        super(callable);
        this.task = callable;
        this.init(config, runtimeInfo, callable);
    }

    public RegularTask(Runnable runnable,
                       T result,
                       ProxyConfiguration config,
                       TaskRuntimeInfo runtimeInfo) {
        super(runnable, result);
        this.task = runnable;
        this.init(config, runtimeInfo, runnable);
    }

    private void init(ProxyConfiguration config,
                      TaskRuntimeInfo runtimeInfo,
                      Object runee) {
        if ((config == null) || (runtimeInfo == null) || (runee == null)) {
            throw new RuntimeException("ProxyConfig, RuntimeInfo or runee can not be null !");
        }
        this.proxyConfig = config;
        this.runtimeInfo = runtimeInfo;
        this.taskClass = runee.getClass().getName();
    }

    @Override
    public void run() {
        boolean success = false;
        try {
            this.running();
            super.run();
            success = true;
        } finally {
            this.runtimeInfo.increaseRun(this.getDelta(), success);
            this.stopping();
        }
    }

    @Override
    protected void done() {
        try {
            this.get();
        } catch (InterruptedException e) {
            logger.error("Interrupted ! " + e.getMessage(), e);
        } catch (ExecutionException e) {
            logger.error("Uncaught Exception on execution : " + e.getCause().getMessage(), e.getCause());
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

    @Override
    public int compareTo(RegularTask<T> task) {
        return this.runtimeInfo.getPriority() - task.getPriority();
    }

    @Override
    public void interrupting() {
        if (this.managed()) {
            ((IManaged) this.task).interrupting();
        }
    }

    @Override
    public long getMaxExecutionTime() {
        if (this.managed()) {
            return ((IManaged) this.task).getMaxExecutionTime();
        }
        return 0;
    }

    private boolean managed() {
        return this.managed || (this.managed = this.task instanceof IManaged);
    }

    //
    @Override
    public String toString() {
        return "Task: " + this.taskClass;
    }
}
