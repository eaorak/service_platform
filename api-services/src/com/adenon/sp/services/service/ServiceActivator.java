package com.adenon.sp.services.service;

import java.util.List;

import org.osgi.framework.Bundle;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.IPoolConfig;
import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.kernel.osgi.activator.Activator;
import com.adenon.sp.kernel.utils.IBundleScanService;
import com.adenon.sp.kernel.utils.log.BundleType;
import com.adenon.sp.services.service.factory.IServiceFactory;
import com.adenon.sp.services.service.info.Service;


public abstract class ServiceActivator extends Activator {

    private static final String POOL_PREFIX = "SRV_";
    private IServiceRegistry    serviceRegistry;
    protected IThreadPool       servicePool;

    public ServiceActivator() {
    }

    @Override
    public BundleType getType() {
        return BundleType.SERVICE;
    }

    @Override
    protected final void start() throws Exception {
        Bundle bundle = this.context.getBundle();
        String bundleName = this.bundleInfo.get(ManifestHeaders.BUNDLE_NAME);
        this.serviceRegistry = this.getService(IServiceRegistry.class);
        IBundleScanService classUtils = this.getService(IBundleScanService.class);
        // Scan for all service classes in service jar and register
        List<Class<?>> serviceClasses = classUtils.getAnnotatedClassesOf(bundle, Service.class);
        if ((serviceClasses == null) || (serviceClasses.size() == 0)) {
            throw new Exception("No service is defined in bundle [" + bundleName + "] !");
        }
        this.servicePool = this.createServicePool(bundleName);
        // ----------------
        this.startService();
        // ----------------
        IServiceFactory serviceFactory = this.getServiceFactory();
        if (serviceFactory == null) {
            throw new Exception("Service factory is null in bundle [" + bundleName + "] !");
        }
        this.serviceRegistry.register(this.bundleInfo, serviceClasses, serviceFactory, this.servicePool);
    }

    private IThreadPool createServicePool(final String bundleName) throws Exception {
        IExecuterManager execManager = this.getService(IExecuterManager.class);
        final IPoolConfig poolConfig = execManager.newConfig(bundleName, POOL_PREFIX + bundleName);
        return execManager.createOrGetPool(poolConfig);
    }

    @Override
    protected final void stop() throws Exception {
        this.serviceRegistry.unregister(this.bundleInfo);
        this.stopService();
    }

    protected abstract void startService() throws Exception;

    protected abstract void stopService() throws Exception;

    protected abstract IServiceFactory getServiceFactory();

}