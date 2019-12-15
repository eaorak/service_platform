package com.adenon.sp.statistics;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;
import com.adenon.sp.monitoring.counters.IMonitoringService;
import com.adenon.sp.persistence.IPersistenceService;

public class StatisticsActivator extends BundleActivator {

    @Override
    protected void start() throws Exception {
        this.getServices(IMonitoringService.class, IPersistenceService.class, IExecuterManager.class);
        StatisticsManager statistics = new StatisticsManager(this.services);
        this.registerService(IStatisticsService.class, statistics);
    }

    @Override
    protected void stop() throws Exception {
    }

}
