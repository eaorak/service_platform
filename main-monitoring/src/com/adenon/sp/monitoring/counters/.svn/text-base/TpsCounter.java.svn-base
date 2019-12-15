package com.adenon.sp.monitoring.counters;

import java.util.concurrent.atomic.AtomicLong;

import com.adenon.sp.administration.annotate.Dynamic;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.kernel.utils.cache.RoundArray;


@MBean(parent = Monitor.class)
@Dynamic(key = "uniqueKey", value = "completeValue")
public class TpsCounter extends Counter implements ITpsCounter {

    protected RoundArray<Long> tpsStats = new RoundArray<Long>(Long.class, 60);
    private final AtomicLong   tpsValue = new AtomicLong();
    private volatile long      lastTps;

    @Deprecated
    public TpsCounter() {
    }

    public TpsCounter(final String key,
                      final Monitor monitor,
                      final ICounter parent) {
        super(key, monitor, parent);
        this.type = CounterType.TPS;
    }

    @Override
    public long increaseBy(final long val) {
        this.tpsValue.addAndGet(val);
        return super.increaseBy(val);
    }

    @Override
    public long getTpsValue() {
        return this.lastTps;
    }

    public long resetTpsValue() {
        this.lastTps = this.tpsValue.getAndSet(0);
        this.tpsStats.put(this.lastTps);
        return this.lastTps;
    }

    public RoundArray<Long> getTpsStats() {
        return this.tpsStats;
    }

    public String getCompleteValue() {
        return this.getTpsValue() + "/" + this.getValue();
    }

    @Override
    public String toString() {
        return "TpsCounter [lastTps=" + this.lastTps + ", " + super.toString() + "]";
    }

}
