package com.adenon.sp.executer.watchdog;

import java.util.concurrent.ScheduledFuture;

import com.adenon.sp.executer.pool.IThreadPoolInternal;
import com.adenon.sp.executer.watchdog.AbstractPoolWatchdog;


public class PoolWatchdog implements Runnable {

    private IThreadPoolInternal  pool;
    private AbstractPoolWatchdog policy;
    private ScheduledFuture<?>   wdFuture;

    public PoolWatchdog(IThreadPoolInternal pool,
                        AbstractPoolWatchdog policy) {
        this.pool = pool;
        this.policy = policy;
        policy.injectPool(pool, pool.getConfiguration().getFactory());
    }

    @Override
    public void run() {
        this.policy.run();
    }

    public void injectFuture(ScheduledFuture<?> wdFuture) {
        this.wdFuture = wdFuture;
    }

    public ScheduledFuture<?> getWdFuture() {
        return this.wdFuture;
    }

    public AbstractPoolWatchdog getPolicy() {
        return this.policy;
    }

    public String getName() {
        return "Watchdog-" + this.pool.getConfiguration().getPoolName();
    }

}
