package com.adenon.sp.monitoring.counters;

public interface ICounter {

    String SEP = "#";

    IMonitor getMonitor();

    ICounter getParent();

    String getUniqueKey();

    String getKey();

    long increase();

    long decrease();

    long increaseBy(long value);

    long decreaseBy(long value);

    long getValue();

    CounterType getType();

    ISnapshot takeSnapshot();


}
