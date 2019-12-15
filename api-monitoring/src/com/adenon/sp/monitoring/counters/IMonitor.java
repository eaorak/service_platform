package com.adenon.sp.monitoring.counters;

public interface IMonitor {

    String getName();

    ICounter createCounter(String key);

    ICounter createCounter(String key,
                           ICounter parent);

    ITpsCounter createTpsCounter(String key);

    ITpsCounter createTpsCounter(String key,
                                 ICounter parent);

    ICounter deleteCounter(String uniqueKey);

    ICounter[] allCounters();

}
