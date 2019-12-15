package com.adenon.sp.executer.pool;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.TMConstants;
import com.adenon.sp.executer.config.PoolConfiguration;
import com.adenon.sp.executer.task.RegularTask;
import com.adenon.sp.executer.task.pool.IPoolInfo;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.executer.watchdog.DefaultWatchdogPolicy;
import com.adenon.sp.executer.watchdog.PoolWatchdog;


public class RegularPool extends ThreadPoolExecutor implements IThreadPoolInternal, IPoolInfo {

    private static final Logger                 logger = Logger.getLogger(RegularPool.class);
    private final PoolConfiguration             poolConfig;
    private final Method                        addThread;
    private PoolWatchdog                        watchdog;
    private final PoolUtil<IThreadPoolInternal> util;

    public static RegularPool createPool(final PoolConfiguration configuration) throws Exception {
        configuration.validate();
        final RegularPool threadPool = new RegularPool(configuration);
        threadPool.prestartAllCoreThreads();
        scheduleWatchdog(configuration, threadPool);
        return threadPool;
    }

    private static void scheduleWatchdog(final PoolConfiguration configuration,
                                         final RegularPool threadPool) {
        final IScheduledPool scheduledPool = configuration.getThreadManager().getScheduledPool(TMConstants.BUNDLE_NAME);
        threadPool.watchdog = new PoolWatchdog(threadPool, new DefaultWatchdogPolicy());
        final ScheduledFuture<?> future = scheduledPool.scheduleWithFixedDelay(threadPool.watchdog, 0, 1, TimeUnit.SECONDS);
        threadPool.watchdog.injectFuture(future);
    }

    @Override
    public PoolUtil<IThreadPoolInternal> util() {
        return this.util;
    }

    private RegularPool(final PoolConfiguration config) throws Exception {
        super(config.getCoreSize(),
              config.getMaxSize(),
              config.getKeepAliveTime(),
              TimeUnit.MILLISECONDS,
              config.getQueue(),
              config.getFactory(),
              config.getRejectHandler());
        this.poolConfig = config;
        this.addThread = this.prepareMethod();
        this.util = new PoolUtil<IThreadPoolInternal>(this);
        //
        config.injectPool(this);
        config.getFactory().injectPool(this);
    }

    private Method prepareMethod() {
        String methodName = "addIfUnderMaximumPoolSize";
        Method m = null;
        try {
            m = ThreadPoolExecutor.class.getDeclaredMethod(methodName, Runnable.class);
        } catch (final Exception e) {
            methodName = "addWorker";
            try {
                m = ThreadPoolExecutor.class.getDeclaredMethod(methodName, new Class[] { Runnable.class, boolean.class });
            } catch (final Exception e1) {
            }
            if (m == null) {
                throw new UnsupportedClassVersionError("Method ["
                                                       + methodName
                                                       + "] could not be found in this version of Java ["
                                                       + System.getProperty("java.version")
                                                       + "] !");
            }
        }
        m.setAccessible(true);
        return m;
    }

    // Overridden executed methods
    @Override
    public void execute(final Runnable command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<?> submit(final Runnable task) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(final Runnable task,
                                final T result) {
        throw new UnsupportedOperationException();
    };

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        throw new UnsupportedOperationException();
    }

    // Execution methods for proxy

    @Override
    public <T> Future<T> executeTask(final RegularTask<T> task) {
        super.execute(task);
        return task;
    }

    //

    @Override
    public void setCorePoolSize(final int corePoolSize) {
        super.setCorePoolSize(corePoolSize);
    }

    @Override
    public void setMaximumPoolSize(final int maximumPoolSize) {
        super.setMaximumPoolSize(maximumPoolSize);
    }

    @Override
    public void setKeepAliveTime(final long time,
                                 final TimeUnit unit) {
        super.setKeepAliveTime(time, unit);
        this.poolConfig.setKeepAliveTime(unit.toMillis(time));
    }

    //
    @Override
    public boolean addNewThread(final int count) {
        boolean success = true;
        try {
            for (int i = 0; i < count; i++) {
                success &= (Boolean) this.addThread.invoke(this, new Object[] { null });
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    @Override
    public PoolConfiguration getConfiguration() {
        return this.poolConfig;
    }

    @Override
    public RegularPoolProxy<?> createProxy(final String bundleName) {
        return new RegularPoolProxy<IThreadPoolInternal>(this, bundleName, this.poolConfig.getConfiguration());
    }

    @Override
    protected void afterExecute(Runnable r,
                                Throwable t) {
        if (t != null) {
            logger.error("Error on task [" + r.getClass() + " :: " + r + "] " + t.getMessage(), t);
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        final List<Runnable> runnables = super.shutdownNow();
        return runnables;
    }

    @Override
    public int getQueueSize() {
        return this.getQueue().size();
    }

    @Override
    public String getQueueGraph() {
        // return this.watchdog.getPolicy().getQueueSizes().visualize(20);
        return "Not implemented yet";
    }

    @Override
    public String getPoolName() {
        return this.poolConfig.getPoolName();
    }

    @Override
    public String toString() {
        return "[ThrPool# " + this.poolConfig.toString() + "]";
    }

}
