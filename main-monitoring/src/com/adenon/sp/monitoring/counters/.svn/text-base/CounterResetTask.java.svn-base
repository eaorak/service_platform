package com.adenon.sp.monitoring.counters;

import java.util.List;

import org.apache.log4j.Logger;

public class CounterResetTask implements Runnable {

    private static Logger    logger = Logger.getLogger(CounterResetTask.class);
    private List<TpsCounter> counters;

    public CounterResetTask(List<TpsCounter> counters) {
        this.counters = counters;
    }

    @Override
    public void run() {
        for (TpsCounter counter : this.counters) {
            try {
                counter.resetTpsValue();
            } catch (Throwable e) {
                logger.error("[CounterResetTask][run] : Error : " + e.getMessage(), e);
            }
        }
    }

}
