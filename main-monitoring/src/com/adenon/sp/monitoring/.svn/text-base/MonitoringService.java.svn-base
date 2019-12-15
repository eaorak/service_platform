package com.adenon.sp.monitoring;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.adenon.sp.administration.AdministrationException;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.kernel.services.IBundleInfoService;
import com.adenon.sp.kernel.utils.IFrameworkServices;
import com.adenon.sp.kernel.utils.log.ILoggingService;
import com.adenon.sp.monitoring.bundles.BundleMonitor;
import com.adenon.sp.monitoring.counters.Counter;
import com.adenon.sp.monitoring.counters.CounterResetTask;
import com.adenon.sp.monitoring.counters.IMonitor;
import com.adenon.sp.monitoring.counters.IMonitoringService;
import com.adenon.sp.monitoring.counters.Monitor;
import com.adenon.sp.monitoring.counters.TpsCounter;


public class MonitoringService implements IMonitoringService {

    private final IAdministrationService administration;
    private final Map<String, Monitor>   monitors    = new ConcurrentHashMap<String, Monitor>();
    private final List<TpsCounter>       tpsCounters = new CopyOnWriteArrayList<TpsCounter>();

    public MonitoringService(final Services services) throws AdministrationException {
        this.administration = services.getService(IAdministrationService.class);
        final CounterResetTask resetterTask = new CounterResetTask(this.tpsCounters);
        //
        String bundleName = services.getBundleInfo().get(ManifestHeaders.BUNDLE_NAME);
        IScheduledPool scheduledPool = services.getService(IExecuterManager.class).getScheduledPool(bundleName);
        scheduledPool.scheduleWithFixedDelay(resetterTask, 0, 1, TimeUnit.SECONDS);
        //
        this.registerBundleInfo(services, this.administration);
    }


    @Override
    public IMonitor createMonitor(final String name) throws Exception {
        Monitor monitor = new Monitor(name, this);
        monitor = this.administration.registerBean(monitor);
        this.monitors.put(name, monitor);
        return monitor;
    }

    @Override
    public IMonitor getMonitor(String name) {
        return this.monitors.get(name);
    }


    @Override
    public IMonitor[] getMonitors() {
        Collection<Monitor> values = this.monitors.values();
        return values.toArray(new IMonitor[values.size()]);
    }

    @Override
    public IMonitor deleteMonitor(final String name) {
        return this.monitors.remove(name);
    }

    private void registerBundleInfo(Services services,
                                    IAdministrationService administration) throws AdministrationException {
        IBundleInfoService infoService = services.getService(IBundleInfoService.class);
        ILoggingService logging = services.getService(IFrameworkServices.class).loggingService();
        BundleMonitor bundleMonitor = new BundleMonitor();
        List<BundleInfo> infoList = infoService.register(bundleMonitor);
        bundleMonitor.initialize(administration, logging, infoList);
    }

    public Counter registerCounter(final Counter counter) throws Exception {
        return this.administration.registerBean(counter);
    }

    public void addTpsCounter(final TpsCounter counter) {
        this.tpsCounters.add(counter);
    }

    public void removeTpsCounter(final TpsCounter counter) {
        this.tpsCounters.remove(counter);
    }

}
