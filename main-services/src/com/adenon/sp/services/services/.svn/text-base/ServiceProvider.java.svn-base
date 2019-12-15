package com.adenon.sp.services.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.services.service.factory.IServiceFactory;
import com.adenon.sp.services.service.info.IServiceModel;
import com.adenon.sp.services.service.info.IServiceProvider;


public class ServiceProvider implements IServiceProvider {

    private final Map<String, IServiceModel> models = new HashMap<String, IServiceModel>();
    private final IServiceFactory            srvFactory;
    private final BundleInfo                 bundleInfo;
    private final IThreadPool                servicePool;

    public ServiceProvider(final BundleInfo bundleInfo, final IServiceFactory serviceFactory, final IThreadPool servicePool) {
        this.bundleInfo = bundleInfo;
        this.srvFactory = serviceFactory;
        this.servicePool = servicePool;
    }

    public void addServiceModel(final ServiceModel serviceModel) {
        final String serviceId = serviceModel.getServiceId();
        final IServiceModel model = this.models.get(serviceId);
        if (model != null) {
            throw new RuntimeException("Service model already exist with id [" + serviceId + "] !");
        }
        this.models.put(serviceId, serviceModel);
    }

    public String getName() {
        return this.bundleInfo.get(ManifestHeaders.BUNDLE_SYMBOLICNAME);
    }

    public String getVersion() {
        return this.bundleInfo.get(ManifestHeaders.BUNDLE_VERSION);
    }

    public IServiceModel getService(final String serviceId) {
        return this.models.get(serviceId);
    }

    @Override
    public IServiceFactory getServiceFactory() {
        return this.srvFactory;
    }

    @Override
    public Collection<IServiceModel> getServiceModels() {
        return this.models.values();
    }

    @Override
    public IThreadPool getServicePool() {
        return this.servicePool;
    }

    @Override
    public BundleInfo getBundleInfo() {
        return this.bundleInfo;
    }

    @Override
    public String toString() {
        return "ServiceBundle [" + this.bundleInfo + "]";
    }

}
