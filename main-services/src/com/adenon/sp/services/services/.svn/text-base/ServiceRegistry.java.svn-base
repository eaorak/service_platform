package com.adenon.sp.services.services;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.kernel.osgi.activator.Activator;
import com.adenon.sp.kernel.utils.IBundleScanService;
import com.adenon.sp.services.service.IServiceRegistry;
import com.adenon.sp.services.service.factory.IServiceFactory;
import com.adenon.sp.services.service.info.IServiceModel;
import com.adenon.sp.services.service.info.IServiceProvider;
import com.adenon.sp.services.service.info.Service;

public class ServiceRegistry implements IServiceRegistry {

    private final Map<String, ServiceProvider> serviceBundles = new ConcurrentHashMap<String, ServiceProvider>();
    private final Map<String, IServiceModel>   serviceModels  = new ConcurrentHashMap<String, IServiceModel>();
    private final Services                     services;
    private final IBundleScanService           classUtils;

    public ServiceRegistry(final Services services) {
        this.services = services;
        this.classUtils = services.getService(IBundleScanService.class);
    }

    @Override
    public IServiceModel getServiceModel(final String serviceName) {
        return this.serviceModels.get(serviceName);
    }

    @Override
    public IServiceModel getServiceModelById(final String serviceBundle, final String serviceId) {
        final ServiceProvider serviceProvider = this.serviceBundles.get(serviceBundle);
        if (serviceProvider == null) {
            return null;
        }
        return serviceProvider.getService(serviceId);
    }

    @Override
    public IServiceProvider register(final Activator activator, final IServiceFactory serviceFactory, final IThreadPool servicePool) throws Exception {
        final List<Class<?>> srvClasses = this.classUtils.getAnnotatedClassesOf(activator.getBundle(), Service.class);
        return this.register(activator.getBundleInfo(), srvClasses, serviceFactory, servicePool);
    }

    @Override
    public IServiceProvider register(final BundleInfo info,
                                     final List<Class<?>> srvClasses,
                                     final IServiceFactory serviceFactory,
                                     final IThreadPool pool) throws Exception {
        if ((info == null) || ((srvClasses == null) || (srvClasses.size() == 0)) || (serviceFactory == null) || (pool == null)) {
            throw new InvalidParameterException("Info, factory, pool and service classes list can not be null or have zero size!");
        }
        final ServiceProvider srvBundle = new ServiceProvider(info, serviceFactory, pool);
        for (final Class<?> service : srvClasses) {
            final ServiceModel serviceModel = new ServiceModel(service, srvBundle, this.services);
            srvBundle.addServiceModel(serviceModel);
        }
        for (final IServiceModel model : srvBundle.getServiceModels()) {
            this.serviceModels.put(model.getServiceClass().getName(), model);
        }
        this.serviceBundles.put(srvBundle.getName(), srvBundle);
        return srvBundle;
    }

    @Override
    public void unregister(final BundleInfo headers) {
        final ServiceProvider srvBundle = this.serviceBundles.remove(headers.get(ManifestHeaders.BUNDLE_SYMBOLICNAME));
        if (srvBundle == null) {
            return;
        }
        final Collection<IServiceModel> serviceModels = srvBundle.getServiceModels();
        for (final IServiceModel model : serviceModels) {
            this.serviceModels.remove(model.getServiceClass().getName());
        }
    }

}
