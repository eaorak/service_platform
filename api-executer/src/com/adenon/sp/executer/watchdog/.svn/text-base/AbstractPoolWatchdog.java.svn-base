package com.adenon.sp.executer.watchdog;

import com.adenon.sp.executer.task.pool.IThreadPoolCore;
import com.adenon.sp.executer.thread.IThreadFactory;

public abstract class AbstractPoolWatchdog extends AbstractWatchdog {

    protected int             MAX_QUEUE_SIZE  = 1000;
    protected int             MIN_QUEUE_BOUND = 5;

    protected IThreadPoolCore pool;
    protected IThreadFactory  factory;

    // protected RoundIntArray queueSizes = new RoundIntArray(100);

    public AbstractPoolWatchdog() {
    }

    public void injectPool(IThreadPoolCore pool,
                           IThreadFactory factory) {
        this.pool = pool;
        this.factory = factory;
    }

}
