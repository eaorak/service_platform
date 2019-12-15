package com.adenon.sp.monitoring.counters;

import java.util.concurrent.atomic.AtomicLong;

import com.adenon.sp.administration.annotate.Dynamic;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.kernel.utils.cache.RoundArray;


@MBean(parent = Monitor.class)
@Dynamic(key = "uniqueKey", value = "value")
public class Counter implements ICounter {

    protected RoundArray<Long>      stats     = new RoundArray<Long>(Long.class, 60);
    private final AtomicLong        value     = new AtomicLong();
    private Monitor                 monitor;
    private ICounter                parent;
    private String                  key;
    private String                  parentKey;
    private String                  uniqueKey;
    protected CounterType           type      = CounterType.REGULAR;
    protected ThreadLocal<Snapshot> snapshots = new ThreadLocal<Snapshot>() {

                                                  @Override
                                                  protected Snapshot initialValue() {
                                                      return new Snapshot(Counter.this);
                                                  }
                                              };

    @Deprecated
    public Counter() {
    }

    public Counter(final String key,
                   final Monitor monitor,
                   final ICounter parent) {
        this.key = key;
        this.monitor = monitor;
        this.parent = parent;
        this.parentKey = this.getParentKey(parent, new StringBuilder()).toString();
        this.uniqueKey = this.parentKey + key;
    }

    @Join
    public String getMonitorName() {
        return this.monitor.getName();
    }

    public StringBuilder getParentKey(final ICounter counter,
                                      final StringBuilder key) {
        if (counter == null) {
            return key;
        }
        final ICounter parent = counter.getParent();
        if (parent != null) {
            this.getParentKey(parent, key);
        }
        key.append(counter.getKey()).append("#");
        return key;
    }

    @Override
    public long increaseBy(final long val) {
        final long newVal = this.value.addAndGet(val);
        if (this.parent != null) {
            this.parent.increaseBy(val);
        }
        return newVal;
    }

    @Override
    public long decreaseBy(final long val) {
        return this.increaseBy(-val);
    }

    @Override
    public long increase() {
        return this.increaseBy(1);
    }

    @Override
    public long decrease() {
        return this.increaseBy(-1);
    }

    public boolean isSubCounterOf(final String parentName) {
        return this.parentKey.startsWith(parentName);
    }

    public ISnapshot takeSnapshot() {
        Snapshot snapshot = this.snapshots.get();
        snapshot.update(this);
        return snapshot;
    }

    //
    @Override
    public long getValue() {
        return this.value.get();
    }

    @Deprecated
    public void setValue(final long value) {
        // Do nothing, for configuration
    }

    @Override
    public IMonitor getMonitor() {
        return this.monitor;
    }

    @Override
    public ICounter getParent() {
        return this.parent;
    }

    @Override
    public String getUniqueKey() {
        return this.uniqueKey;
    }

    @Override
    public CounterType getType() {
        return this.type;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public String getParentKey() {
        return this.parentKey;
    }

    public RoundArray<Long> getStats() {
        return this.stats;
    }

    @Override
    public String toString() {
        return "Counter [uniqueKey=" + this.uniqueKey + ", currentValue=" + this.getValue() + "]";
    }


}
