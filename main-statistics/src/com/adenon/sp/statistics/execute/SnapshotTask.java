package com.adenon.sp.statistics.execute;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.monitoring.counters.ICounter;
import com.adenon.sp.monitoring.counters.IMonitor;
import com.adenon.sp.monitoring.counters.IMonitoringService;
import com.adenon.sp.persistence.IPersistenceService;
import com.adenon.sp.persistence.SystemPool;
import com.adenon.sp.statistics.StatisticsManager;
import com.adenon.sp.statistics.info.MonitorStats;


public class SnapshotTask implements Runnable {

    private static final Logger      logger = Logger.getLogger(SnapshotTask.class);
    private final StatisticsManager  manager;
    private final IMonitoringService monitoring;
    private final EntityManager      entityManager;
    private final IThreadPool        statsPool;


    public SnapshotTask(StatisticsManager manager,
                        IThreadPool statsPool,
                        Services services) {
        this.manager = manager;
        this.statsPool = statsPool;
        this.monitoring = services.getService(IMonitoringService.class);
        this.entityManager = services.getService(IPersistenceService.class).getSystemPool(SystemPool.STATS).getEntityManager();
    }

    @Override
    public void run() {
        try {
            for (MonitorStats stats : this.manager.getStats()) {
                if (!stats.takeSnapshot()) {
                    continue;
                }
                IMonitor monitor = this.monitoring.getMonitor(stats.getMonitorName());
                if (logger.isDebugEnabled()) {
                    logger.debug("[StatisticsManager][getStats] : Taking statistics for " + monitor + " ..");
                }
                stats.setSnapshotTime();
                for (ICounter counter : monitor.allCounters()) {
                    stats.addSnapshot(counter.takeSnapshot());
                }
                this.statsPool.submit(new PersistTask(this.entityManager, stats));
            }
        } catch (Exception e) {
            logger.error("[StatisticPersister][run] : Error : " + e.getMessage(), e);
        }
    }


}
