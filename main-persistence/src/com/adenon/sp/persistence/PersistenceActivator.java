package com.adenon.sp.persistence;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;

public class PersistenceActivator extends BundleActivator {

    @Override
    protected void start() throws Exception {
        this.getServices(IAdministrationService.class);
        PersistenceService persistence = new PersistenceService(this.services, this.confPath);
        this.registerService(IPersistenceService.class, persistence);
    }

    @Override
    protected void stop() throws Exception {
    }

}