package com.adenon.sp.monitoring.counters;

public class Snapshot implements ISnapshot {

    private final String      monitorName;
    private final String      key;
    private long              value;
    private long              tpsValue;
    private long              time;
    private final CounterType type;

    public Snapshot(Counter counter) {
        this.monitorName = counter.getMonitorName();
        this.key = counter.getKey();
        this.type = counter.getType();
    }

    public void update(Counter counter) {
        this.value = counter.getValue();
        this.time = System.currentTimeMillis();
    }

    public void update(TpsCounter counter) {
        this.update((Counter) counter);
        this.tpsValue = counter.getTpsValue();
    }

    @Override
    public String getMonitorName() {
        return this.monitorName;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public long getValue() {
        return this.value;
    }

    @Override
    public long getTpsValue() {
        return this.tpsValue;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public CounterType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Snapshot [name=" + this.monitorName + "#" + this.key + ", tps | value =" + this.tpsValue + " | " + this.value + ", type=" + this.type + "]";
    }

}