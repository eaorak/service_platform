package com.adenon.sp.streams;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;


public class StreamsActivator extends BundleActivator {

    @Override
    protected void start() throws Exception {
        this.getService(IExecuterManager.class);
        this.registerService(IStreamService.class, new StreamService(this.services));
    }

    @Override
    protected void stop() throws Exception {

    }

}
