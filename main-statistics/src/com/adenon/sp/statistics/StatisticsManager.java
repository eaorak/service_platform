package com.adenon.sp.statistics;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.IPoolConfig;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.statistics.execute.SnapshotTask;
import com.adenon.sp.statistics.info.MonitorStats;

public class StatisticsManager implements IStatisticsService {

    private final List<MonitorStats> monitorStats = new CopyOnWriteArrayList<MonitorStats>();
    private final IExecuterManager   executer;

    public StatisticsManager(Services services) throws Exception {
        this.executer = services.getService(IExecuterManager.class);
        //
        IPoolConfig config = this.executer.newConfig(services.getBundleInfo().get(ManifestHeaders.BUNDLE_NAME), "Statistics").setCoreSize(10).setMaxSize(20);
        IThreadPool statsPool = this.executer.createOrGetPool(config);
        //
        IScheduledPool scheduledPool = this.executer.getScheduledPool(services.getBundleInfo().get(ManifestHeaders.BUNDLE_NAME));
        this.test();
        scheduledPool.scheduleWithFixedDelay(new SnapshotTask(this, statsPool, services), 0, 1, TimeUnit.MINUTES);
    }

    private void test() {
        this.monitorStats.add(new MonitorStats("Channels", TimeUnit.MINUTES, 1));
    }

    public List<MonitorStats> getStats() {
        return this.monitorStats;
    }

}
