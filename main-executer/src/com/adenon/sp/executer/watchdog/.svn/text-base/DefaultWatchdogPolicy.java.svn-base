package com.adenon.sp.executer.watchdog;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.task.pool.IManagedInternal;
import com.adenon.sp.executer.thread.IThread;
import com.adenon.sp.executer.watchdog.AbstractPoolWatchdog;


public class DefaultWatchdogPolicy extends AbstractPoolWatchdog {

    private static Logger logger = Logger.getLogger(DefaultWatchdogPolicy.class);

    public DefaultWatchdogPolicy() {
    }

    @Override
    public void initWatchDog() throws Exception {
    }

    @Override
    public void doActions() {
        try {
            this.optimizePool();
            this.checkThreads();
        } catch (Exception e) {
            logger.error("[DefaultWatchdogPolicy][run] : Error on processing pool : " + this.pool.getConfiguration().getPoolName() + " ! " + e.getMessage(), e);
        }
    }

    private void optimizePool() {
        int queueSize = this.pool.getQueueSize();
        if (queueSize > this.MIN_QUEUE_BOUND) {
            int currentSize = this.pool.getPoolSize();
            int maxSize = this.pool.getMaximumPoolSize();
            //
            int addCount = (queueSize / this.MIN_QUEUE_BOUND);
            addCount = ((currentSize + addCount) > maxSize) ? (maxSize - currentSize) : addCount;
            //
            if (logger.isInfoEnabled()) {
                logger.info("[DefaultWatchdogPolicy][run] : ["
                            + queueSize
                            + "] waiting requests. Increasing pool size from ["
                            + currentSize
                            + "] to ["
                            + addCount
                            + "]");
            }
            this.pool.addNewThread(addCount);
        }
    }

    private void checkThreads() {
        for (IThread thread : this.factory.getThreads()) {
            IManagedInternal task = thread.getCurrentTask();
            if (task == null) {
                continue;
            }
            long delta = task.getDelta();
            long maxExecTime = task.getMaxExecutionTime();
            if ((maxExecTime > 0) && (delta > maxExecTime)) {
                thread.interrupt();
                this.pool.addNewThread(1);
                this.log(thread, task, delta, maxExecTime);
            }
        }
    }

    private void log(IThread thread,
                     IManagedInternal task,
                     long delta,
                     long maxExecTime) {
        logger.error("[DefaultWatchdogPolicy][run] : Task '"
                     + task.getTaskClass()
                     + "' exceeds maxiumum execution time ["
                     + maxExecTime
                     + " : "
                     + delta
                     + " ms]. Interrupted thread '"
                     + thread.getName()
                     + "' and add a new one !");
    }

    @Override
    public long getPeriodInMillis() {
        return TimeUnit.SECONDS.toMillis(1);
    }

}
