package com.adenon.sp.executer.pool.schedule;

import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.executer.config.ScheduleTaskRuntimeInfo;
import com.adenon.sp.executer.pool.TFuture;


public class TScheduledFuture<V> extends TFuture<V> implements ScheduledFuture<V> {

    public TScheduledFuture(ScheduledFuture<V> schFuture,
                            ScheduleTaskRuntimeInfo task) {
        super(schFuture, task);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return ((ScheduledFuture<?>) this.future).getDelay(unit);
    }

    @Override
    public int compareTo(Delayed o) {
        return ((ScheduledFuture<?>) this.future).compareTo(o);
    }

}
