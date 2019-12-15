package com.adenon.sp.executer.watchdog;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.IExecuterManagerInternal;


public abstract class AbstractWatchdog implements Runnable {

    protected IExecuterManagerInternal manager;
    protected Logger                   logger = Logger.getLogger(this.getClass());

    public AbstractWatchdog() {
    }

    @Override
    public void run() {
        try {
            this.doActions();
        } catch (Exception e) {
            this.logger.error("[AbstractWatchdog][run] : Error : " + e.getMessage(), e);
        } finally {

        }
    }

    public abstract void initWatchDog() throws Exception;

    public abstract void doActions();

    /**
     * Execution period of the watchdog. Default time is 1 second.
     */
    public abstract long getPeriodInMillis();

}
