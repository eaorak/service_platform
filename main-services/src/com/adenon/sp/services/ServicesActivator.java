package com.adenon.sp.services;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.kernel.dialog.IDialogCache;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;
import com.adenon.sp.kernel.utils.IBundleScanService;
import com.adenon.sp.services.execution.ServiceExecutionManager;
import com.adenon.sp.services.service.IServiceRegistry;
import com.adenon.sp.services.service.info.IServiceExecution;
import com.adenon.sp.services.services.ServiceRegistry;
import com.adenon.sp.streams.IStreamService;
import com.adenon.sp.streams.Priority;

public class ServicesActivator extends BundleActivator {

    @Override
    protected void start() throws Exception {
        this.getServices(IBundleScanService.class, IExecuterManager.class, IDialogCache.class);
        final IStreamService streamService = this.getService(IStreamService.class);
        // Prepare managers
        final IServiceRegistry registry = this.registerService(IServiceRegistry.class, new ServiceRegistry(this.services));
        final IServiceExecution execManager = new ServiceExecutionManager(registry, this.services);
        //
        this.registerService(IServiceExecution.class, execManager);
        //
        streamService.register(new ServicesEventProcessor(this.getService(IServiceExecution.class)), Direction.TOWARDS_IN, Priority.LAST);
    }

    @Override
    protected void stop() throws Exception {
    }

}