package com.adenon.sp.executer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.executer.IExecuterManagerInternal;
import com.adenon.sp.executer.IPoolConfig;
import com.adenon.sp.executer.config.PoolConfiguration;
import com.adenon.sp.executer.pool.IThreadPoolInternal;
import com.adenon.sp.executer.pool.RegularPool;
import com.adenon.sp.executer.pool.schedule.IScheduledPoolInternal;
import com.adenon.sp.executer.pool.schedule.SchedulePool;
import com.adenon.sp.executer.task.cron.ICronPool;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.Services;


public class ThreadManager implements IExecuterManagerInternal {

    private static final Logger                  logger             = Logger.getLogger(ThreadManager.class);
    private static final int                     DEFAULT_CORE_SIZE  = 100;
    private static final int                     DEFAULT_MAX_SIZE   = 1000;
    private static final int                     SCHEDULE_CORE_SIZE = 50;
    private IScheduledPoolInternal               scheduledPool;
    private ICronPool                            cronPool;
    private final Map<String, RegularPool>       poolMapping        = new ConcurrentHashMap<String, RegularPool>();
    private final Map<String, PoolConfiguration> poolBeans          = new ConcurrentHashMap<String, PoolConfiguration>();
    private final IAdministrationService         configuration;

    public ThreadManager(Services services) throws Exception {
        // TODO: Come back soon
        this.configuration = services.getService(IAdministrationService.class);
        IBeanHelper<PoolConfiguration> beans = this.configuration.getBeans(PoolConfiguration.class);
        for (PoolConfiguration conf : beans.getAllBeans()) {
            this.poolBeans.put(conf.getPoolName(), conf);
        }
        // Scheduler first
        this.initScheduler();
        this.initDefault();
    }

    private void initDefault() throws Exception {
        PoolConfiguration conf = this.poolBeans.get(DefaultPools.SYSTEM.poolName());
        if (conf == null) {
            conf = new PoolConfiguration(TMConstants.BUNDLE_NAME, DefaultPools.SYSTEM.poolName(), this);
            conf.setCoreSize(DEFAULT_CORE_SIZE);
            conf.setMaxSize(DEFAULT_MAX_SIZE);
        }
        conf.preserveSizes(true);
        conf.injectManager(this);
        conf = this.configuration.registerBean(conf);
        //
        RegularPool pool = RegularPool.createPool(conf);
        this.poolMapping.put(DefaultPools.SYSTEM.poolName(), pool);
    }

    private void initScheduler() throws Exception {
        PoolConfiguration conf = this.poolBeans.get(DefaultPools.SCHEDULER.poolName());
        if (conf == null) {
            conf = new PoolConfiguration(TMConstants.BUNDLE_NAME, DefaultPools.SCHEDULER.poolName(), this);
            conf.setCoreSize(SCHEDULE_CORE_SIZE);
        }
        conf.preserveSizes(true);
        conf.injectManager(this);
        conf = this.configuration.registerBean(conf);
        //
        this.scheduledPool = SchedulePool.createPool(conf);
    }

    @Override
    public IThreadPool getDefaultPool(String bundleName) {
        return this.poolMapping.get(DefaultPools.SYSTEM.poolName()).util().createOrGetProxy(IThreadPool.class, bundleName);
    }

    @Override
    public IScheduledPool getScheduledPool(String bundleName) {
        return this.scheduledPool.util().createOrGetProxy(IScheduledPool.class, bundleName);
        // return this.scheduledPool;
    }

    @Override
    public IThreadPoolInternal getDefaultPool() {
        return this.poolMapping.get(DefaultPools.SYSTEM.poolName());
    }

    @Override
    public IScheduledPoolInternal getScheduledPool() {
        return this.scheduledPool;
    }

    @Override
    public IThreadPool createPool(IPoolConfig config) throws Exception {
        return this.create(config, true);
    }

    @Override
    public IThreadPool createOrGetPool(IPoolConfig config) throws Exception {
        return this.create(config, false);
    }

    @Override
    public IThreadPool getPool(String poolName,
                               String bundleName) {
        RegularPool pool = this.poolMapping.get(poolName);
        if (pool == null) {
            throw new RuntimeException("No associated thread pool can be found for bundle: '" + bundleName + "' !");
        }
        return pool.util().createOrGetProxy(IThreadPool.class, bundleName);
    }

    private synchronized IThreadPool create(IPoolConfig config,
                                            boolean failIfExist) throws Exception {
        if (logger.isInfoEnabled()) {
            String log = failIfExist ? "" : "or get ";
            logger.info("[ThreadManager][createPool] : New pool will be created " + log + "with configuration : " + config);
        }
        String poolName = config.getPoolName();
        RegularPool threadPool = this.poolMapping.get(poolName);
        if (threadPool == null) {
            PoolConfiguration poolConf = this.poolBeans.get(poolName);
            poolConf = poolConf != null ? poolConf : (PoolConfiguration) config;
            poolConf.injectManager(this);
            threadPool = RegularPool.createPool(poolConf);
            poolConf = this.configuration.registerBean(poolConf);
            this.poolMapping.put(poolName, threadPool);
            if (logger.isInfoEnabled()) {
                logger.info("[ThreadManager][create] : New pool with name '" + poolName + "' has been created.");
            }
        } else {
            if (failIfExist) {
                logger.error("[ThreadManager][create] : Error ! A pool with name : " + poolName + " already exists !");
                throw new Exception("Pool with specified name [" + poolName + "] is already exist !");
            }
        }
        return threadPool.util().createOrGetProxy(IThreadPool.class, config.getBundleName());
    }

    //
    @Override
    public IThreadPoolInternal getPool(String poolName) {
        return this.poolMapping.get(poolName);
    }

    @Override
    public IPoolConfig newConfig(String bundleName,
                                 String poolName) {
        return new PoolConfiguration(bundleName, poolName);
    }

    @Override
    public boolean deletePool(String poolName) {
        if (!DefaultPools.canDelete(poolName)) {
            throw new RuntimeException("Invalid pool name or [" + poolName + "] is a system pool and can not be deleted !");
        }
        if (logger.isInfoEnabled()) {
            logger.info("[ThreadManager][deletePool] : Pool with name '" + poolName + "' is being deleted and shutdown !");
        }
        RegularPool pool = this.poolMapping.get(poolName);
        if (pool == null) {
            throw new RuntimeException("No pool exists with name '" + poolName + "' !");
        }
        pool.shutdown();
        return true;
    }

    @SuppressWarnings("unused")
    private RegularPool findPool(String bundleName) {
        for (String regex : this.poolMapping.keySet()) {
            if (bundleName.matches(regex)) {
                return this.poolMapping.get(regex);
            }
        }
        return null;
    }

    @Override
    public ICronPool getCronPool(String bundleName) {
        return this.cronPool;
    }

    @Override
    public IAdministrationService getConfiguration() {
        return this.configuration;
    }

}
