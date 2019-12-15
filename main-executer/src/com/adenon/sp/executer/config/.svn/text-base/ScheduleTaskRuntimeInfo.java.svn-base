package com.adenon.sp.executer.config;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.executer.pool.TFuture;
import com.adenon.sp.executer.pool.schedule.SchedulePoolProxy;
import com.adenon.sp.executer.pool.schedule.TScheduledFuture;
import com.adenon.sp.executer.task.SchType;
import com.adenon.sp.executer.task.ScheduleTask;
import com.adenon.sp.executer.watchdog.PoolWatchdog;


@MBean(parent = ProxyConfiguration.class, id = "taskName")
public class ScheduleTaskRuntimeInfo extends TaskRuntimeInfo {

    private long            initialDelay;
    private long            delay;
    private TimeUnit        unit;
    private long            period;
    private SchType         type;
    private ScheduleTask<?> task;

    @Deprecated
    public ScheduleTaskRuntimeInfo() {
    }

    public static ScheduleTaskRuntimeInfo create(Object command,
                                                 SchedulePoolProxy pool,
                                                 SchType type) {
        String taskName = generateTaskName(command);
        if (command instanceof PoolWatchdog) {
            taskName = ((PoolWatchdog) command).getName();
        }
        return new ScheduleTaskRuntimeInfo(taskName, pool, type);
    }

    private ScheduleTaskRuntimeInfo(String taskName,
                                    SchedulePoolProxy pool,
                                    SchType type) {
        super(taskName, pool);
        this.type = type;
    }

    public String getTaskInfo() {
        return this.type.toString(this);
    }

    public void setTaskInfo(String info) {
        // Do nothing, for configuration
    }

    public <V> ScheduledFuture<V> injectFuture(Future<V> future,
                                               ScheduleTask<?> task) {
        this.task = task;
        return (ScheduledFuture<V>) super.injectFuture(future);
    }

    @Override
    public <V> TFuture<V> createFuture(Future<V> future) {
        return new TScheduledFuture<V>((ScheduledFuture<V>) future, this);
    }

    public String changePeriod(int period) {
        boolean cancelled = this.future.cancel(true, false);
        return this.scheduleAgain(period, cancelled);
    }

    private String scheduleAgain(int period,
                                 boolean cancelled) {
        long preInitDelay = this.getInitialDelay();
        TimeUnit preUnit = this.getUnit();
        long prePeriod = this.getPeriod();
        long preDelay = this.getDelay();
        String result = this.msg("Previous task has{}been cancelled. ", (cancelled ? " " : " NOT "));
        try {
            this.setUnit(TimeUnit.MILLISECONDS);
            this.setInitialDelay(0);
            this.setPeriod(period);
            this.setDelay(period);
            ((SchedulePoolProxy) this.pool).schedule(this.task);
            result += "New task has scheduled. ";
        } catch (Exception e) {
            this.setUnit(preUnit);
            this.setPeriod(prePeriod);
            this.setDelay(preDelay);
            ((SchedulePoolProxy) this.pool).schedule(this.task);
            result += "New task could NOT be scheduled ! Old task has scheduled again !";
        } finally {
            this.setInitialDelay(preInitDelay);
        }
        return result += " " + this.getTaskInfo();
    }

    private String msg(String message,
                       Object... params) {
        String msg = message;
        for (Object param : params) {
            msg = msg.replaceFirst("\\{\\}", param.toString());
        }
        return msg;
    }

    //
    public long getInitialDelay() {
        return this.initialDelay;
    }

    public ScheduleTaskRuntimeInfo setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public long getDelay() {
        return this.delay;
    }

    public ScheduleTaskRuntimeInfo setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    @Attribute
    public long getPeriod() {
        return this.period;
    }

    public ScheduleTaskRuntimeInfo setPeriod(long period) {
        this.period = period;
        return this;
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public ScheduleTaskRuntimeInfo setUnit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    public String getType() {
        return this.type.name();
    }

    public SchType getScheduleType() {
        return this.type;
    }

}
