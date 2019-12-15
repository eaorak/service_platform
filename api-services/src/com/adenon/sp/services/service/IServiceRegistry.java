package com.adenon.sp.services.service;

import java.util.List;

import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.osgi.activator.Activator;
import com.adenon.sp.services.service.factory.IServiceFactory;
import com.adenon.sp.services.service.info.IServiceModel;
import com.adenon.sp.services.service.info.IServiceProvider;


public interface IServiceRegistry {

    /**
     * Get service model by class name.
     */
    IServiceModel getServiceModel(String serviceName);

    /**
     * Get service model by service bundle name and defined service id <br>
     * [by @Service annotation].
     */
    IServiceModel getServiceModelById(String serviceBundle,
                                      String serviceId);


    /**
     * For service bundles.
     */
    IServiceProvider register(BundleInfo bundleInfo,
                              List<Class<?>> classes,
                              IServiceFactory serviceFactory,
                              IThreadPool servicePool) throws Exception;

    /**
     * For channel users (any bundles other than services that want to use channels).
     */
    IServiceProvider register(Activator activator,
                              IServiceFactory serviceFactory,
                              IThreadPool servicePool) throws Exception;

    /**
     * Unregister service bundle.
     */
    void unregister(BundleInfo headers);

}
