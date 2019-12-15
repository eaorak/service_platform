package com.adenon.sp.executer.config;

import java.lang.reflect.Method;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.executer.pool.RegularPoolProxy;
import com.adenon.sp.executer.pool.TFuture;
import com.adenon.sp.executer.task.IRuntimeInfo;
import com.adenon.sp.executer.task.TaskId;
import com.adenon.sp.kernel.utils.cache.RoundArray;


@MBean(parent = ProxyConfiguration.class, id = "taskName")
public class TaskRuntimeInfo implements IRuntimeInfo {

    private String                taskName;
    private final AtomicLong      runCount = new AtomicLong(0);
    private final AtomicLong      failure  = new AtomicLong(0);
    private int                   priority;
    //
    private RoundArray<Long>      execTimes;
    protected TFuture<?>          future;
    protected RegularPoolProxy<?> pool;
    private final boolean         deleted  = false;

    @Deprecated
    public TaskRuntimeInfo() {
    }

    public TaskRuntimeInfo(String taskName,
                           RegularPoolProxy<?> pool) {
        this.taskName = taskName;
        this.pool = pool;
        ProxyConfiguration config = pool.getConfig();
        this.execTimes = new RoundArray<Long>(Long.class, config.getSampleCount());
        this.priority = config.getPriority();
    }

    @Join
    public String getBundleName() {
        return this.pool.getBundleName();
    }

    protected static String generateTaskName(Object command) {
        Class<?> taskClass = command.getClass();
        String taskId = null;
        for (Method m : taskClass.getDeclaredMethods()) {
            TaskId taskAnt = m.getAnnotation(TaskId.class);
            if (taskAnt != null) {
                try {
                    taskId = (String) m.invoke(command, (Object[]) null);
                } catch (Exception e) {
                    throw new RuntimeException("Error occured while getting task id from method : "
                                               + m.getName()
                                               + " ! Task id methods should take no arguments and return String value !");
                }
            }
        }
        return taskClass.getSimpleName() + (taskId == null ? "" : "-" + taskId);
    }

    @SuppressWarnings("unchecked")
    public synchronized <V> Future<V> injectFuture(Future<V> future) {
        if (this.future == null) {
            this.future = this.createFuture(future);
        } else {
            ((TFuture<?>) this.future).injectFuture(future);
        }
        return (Future<V>) this.future;
    }

    public <V> TFuture<V> createFuture(Future<V> future) {
        return new TFuture<V>(future, this);
    }

    public Future<?> getFuture() {
        return this.future;
    }

    @Override
    public long increaseRun(long runDelta,
                            boolean success) {
        long count = this.runCount.incrementAndGet();
        if (!success) {
            this.failure.incrementAndGet();
        }
        this.execTimes.put(runDelta);
        return count;
    }

    @Attribute(readOnly = true)
    @Override
    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Attribute
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Attribute(readOnly = true)
    @Override
    public long getRunCount() {
        return this.runCount.get();
    }

    public void setRunCount(long runCount) {
        // Do nothing, exists for configuration
    }

    @Override
    public long getSuccessCount() {
        return this.runCount.get() - this.failure.get();
    }

    public void setSuccessCount(long runCount) {
        // Do nothing, exists for configuration
    }

    @Attribute(readOnly = true)
    @Override
    public long getFailureCount() {
        return this.failure.get();
    }

    public void setFailureCount(long runCount) {
        // Do nothing, exists for configuration
    }

    @Override
    public long getAverageTime() {
        int count = 0;
        long total = 0;
        for (Long t : this.execTimes.getAll()) {
            if (t != null) {
                total += t;
                count++;
            }
        }
        return count == 0 ? 0 : total / count;
    }

    public void setAverageTime(long time) {
        // Do nothing, exist for configuration
    }

    public String resetAverageTime() {
        this.execTimes.reset(0L);
        return "Average times has been reset.";
    }

    public String showTimeGraph() {
        String nl = System.getProperty("line.separator");
        return ">> Average execution times [Avg : " + this.getAverageTime() + "] <<" + nl + nl + this.execTimes.visualize(20);
    }

}
