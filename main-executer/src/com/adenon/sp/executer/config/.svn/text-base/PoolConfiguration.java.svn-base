package com.adenon.sp.executer.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.executer.IExecuterManagerInternal;
import com.adenon.sp.executer.IPoolConfig;
import com.adenon.sp.executer.pool.IThreadPoolInternal;
import com.adenon.sp.executer.pool.helper.PoolRejectionHandler;
import com.adenon.sp.executer.pool.helper.SpThreadFactory;


@MBean(location = ConfigLocation.SYSTEM, subLocation = "pool=Process Manager", id = "poolName", persist = true)
public class PoolConfiguration implements IPoolConfig {

    private String                   bundleName;
    private String                   poolName;
    private int                      coreSize;
    private int                      maxSize;
    private long                     keepAliveTime = TimeUnit.MINUTES.toMillis(10);
    //
    private int                      initCoreSize  = -1;
    private int                      initMaxSize   = -1;
    private SpThreadFactory          factory;
    private BlockingQueue<Runnable>  queue;
    private RejectedExecutionHandler rejectHandler;
    private IThreadPoolInternal      pool;
    private IExecuterManagerInternal threadManager;
    private boolean                  preserve;

    public PoolConfiguration() {
    }

    public PoolConfiguration(String bundleName, String poolName) {
        this(bundleName, poolName, null);
    }

    public PoolConfiguration(String bundleName, String poolName, IExecuterManagerInternal threadManager) {
        this.bundleName = bundleName;
        this.poolName = poolName;
        if ((this.bundleName == null) || (this.poolName == null)) {
            throw new RuntimeException("Bundle and pool names can not be null !");
        }
        this.threadManager = threadManager;
    }

    // --+-+-+-+-+ Configuration Properties +-+-+-+-+--

    @Override
    @Attribute(name = "Bundle name", description = "Name of the bundle", readOnly = true)
    public String getBundleName() {
        return this.bundleName;
    }

    @Deprecated
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    @Override
    @Attribute(name = "Pool name", description = "Name of the pool", readOnly = true)
    public String getPoolName() {
        return this.poolName;
    }

    @Deprecated
    public void setPoolName(String name) {
        this.poolName = name;
    }

    public int getInitCoreSize() {
        return this.initCoreSize;
    }

    public IPoolConfig setInitCoreSize(int coreSize) {
        return this;
    }

    @Attribute(name = "Core size")
    @Override
    public int getCoreSize() {
        return this.coreSize;
    }

    public void preserveSizes(boolean preserve) {
        this.preserve = preserve;
    }

    @Override
    public synchronized IPoolConfig setCoreSize(int coreSize) {
        if (this.initCoreSize == -1) {
            this.initCoreSize = coreSize;
        }
        if (this.preserve && (coreSize < this.initCoreSize)) {
            throw new RuntimeException("Size of this pool can not be lower than constant core size of ["
                                       + this.initCoreSize
                                       + "] ! Parameter: ["
                                       + coreSize
                                       + "].");
        }
        if (this.pool != null) {
            this.pool.setCorePoolSize(coreSize);
        }
        this.coreSize = coreSize;
        if (coreSize > this.maxSize) {
            this.maxSize = coreSize;
        }
        return this;
    }

    @Attribute(name = "Max size")
    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public IPoolConfig setMaxSize(int maxSize) {
        if (maxSize < this.coreSize) {
            throw new RuntimeException("Max size can not be lower than core size !");
        }
        if (this.initMaxSize == -1) {
            this.initMaxSize = maxSize;
        }
        if (this.preserve && (maxSize > this.initMaxSize)) {
            throw new RuntimeException("Size of this pool can not be bigger than constant max size of ["
                                       + this.initMaxSize
                                       + "] ! Parameter: ["
                                       + maxSize
                                       + "].");
        }
        if (this.pool != null) {
            this.pool.setMaximumPoolSize(maxSize);
        }
        this.maxSize = maxSize;
        return this;
    }

    public int getCurrentSize() {
        return this.pool == null ? 0 : this.pool.getPoolSize();
    }

    public void setCurrentSize(int size) {
        // Do nothing, for configuration
    }

    public int getQueueSize() {
        return this.pool == null ? 0 : this.pool.getQueueSize();
    }

    public void setQueueSize(int size) {
        // Do nothing, for configuration
    }

    @Attribute(name = "Keep alive time")
    public long getKeepAliveTime() {
        return this.keepAliveTime;
    }

    public IPoolConfig setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public String getRejectHandlerType() {
        return this.getType(this.rejectHandler);
    }

    public void setRejectHandlerType(String type) {
        // Do nothing
    }

    @Operation
    public String getThreadInfo() {
        return this.factory.getThreadInfo();
    }

    @Operation
    public String getStackTraceOf(@Parameter(name = "Thread Id") int threadId) {
        return this.factory.getStackTrace(threadId);
    }

    @Operation
    public String kill(@Parameter(name = "Thread Id") int threadId, @Parameter(name = "Confirm") String confirm) {
        if ((confirm == null) || !confirm.equals("YES")) {
            return "Operation not confirmed ! Write 'YES' ONLY IF you REALLY know what you do !";
        }
        return this.factory.kill(threadId);
    }

    @Operation
    public String showQueueGraph() {
        String nl = System.getProperty("line.separator");
        return ">> Queue sizes <<" + nl + nl + this.pool.getQueueGraph();
    }

    // -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-

    private String getType(Object o) {
        return o == null ? "N/A" : o.getClass().getName();
    }

    public BlockingQueue<Runnable> getQueue() {
        return this.queue;
    }

    public IPoolConfig setQueue(BlockingQueue<Runnable> queue) {
        this.queue = queue;
        return this;
    }

    @Override
    public RejectedExecutionHandler getRejectHandler() {
        return this.rejectHandler;
    }

    @Override
    public IPoolConfig setRejectHandler(RejectedExecutionHandler rejectHandler) {
        this.rejectHandler = rejectHandler;
        return this;
    }

    public SpThreadFactory getFactory() {
        return this.factory;
    }

    public void injectPool(IThreadPoolInternal pool) {
        this.pool = pool;
    }

    public void injectManager(IExecuterManagerInternal threadManager) {
        this.threadManager = threadManager;
    }

    public IAdministrationService getConfiguration() {
        return this.threadManager.getConfiguration();
    }

    public IExecuterManagerInternal getThreadManager() {
        return this.threadManager;
    }

    public PoolConfiguration validate() {
        if (this.getFactory() == null) {
            this.factory = new SpThreadFactory(this.poolName);
        }
        if (this.getQueue() == null) {
            this.setQueue(new PriorityBlockingQueue<Runnable>());
        }
        if (this.getRejectHandler() == null) {
            this.setRejectHandler(new PoolRejectionHandler());
        }
        if (this.getCoreSize() == 0) {
            this.setCoreSize(5);
        }
        if (this.getMaxSize() == 0) {
            this.setMaxSize(this.getCoreSize() + 10);
        }
        return this;
    }

    //
    @Override
    public String toString() {
        return "[Config# Pool:'"
               + this.poolName
               + "' Bundle:'"
               + this.bundleName
               + "' Core:"
               + this.getCoreSize()
               + " Max:"
               + this.getMaxSize()
               + " Queue:'"
               + (this.queue == null ? "null" : this.queue.getClass().getName())
               + "' ]";
    }

}
