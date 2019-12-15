package com.adenon.sp.executer.task;

import java.util.concurrent.TimeUnit;

public class ScheduleTaskInfo {

    private long     initialDelay;
    private long     delay;
    private TimeUnit unit;
    private long     period;
    private SchType  schType;

    public ScheduleTaskInfo(SchType schType) {
        this.schType = schType;
    }

    public long getInitialDelay() {
        return this.initialDelay;
    }

    public ScheduleTaskInfo setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public long getDelay() {
        return this.delay;
    }

    public ScheduleTaskInfo setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public ScheduleTaskInfo setUnit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    public ScheduleTaskInfo setPeriod(long period) {
        this.period = period;
        return this;
    }

    public long getPeriod() {
        return this.period;
    }

    public ScheduleTaskInfo setSchType(SchType schType) {
        this.schType = schType;
        return this;
    }

    public SchType getSchType() {
        return this.schType;
    }

}
