package com.adenon.sp.monitoring;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;
import com.adenon.sp.kernel.services.IBundleInfoService;
import com.adenon.sp.kernel.utils.IFrameworkServices;
import com.adenon.sp.monitoring.counters.IMonitoringService;


public class MonitoringActivator extends BundleActivator {

    @Override
    protected void start() throws Exception {
        this.getServices(IAdministrationService.class, IExecuterManager.class, IBundleInfoService.class, IFrameworkServices.class);
        this.registerService(IMonitoringService.class, new MonitoringService(this.services));
    }

    @Override
    protected void stop() throws Exception {
    }

}
