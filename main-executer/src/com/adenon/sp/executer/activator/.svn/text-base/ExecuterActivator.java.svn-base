package com.adenon.sp.executer.activator;

import org.apache.log4j.Logger;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.ThreadManager;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;


public class ExecuterActivator extends BundleActivator {

    private static Logger logger = Logger.getLogger(ExecuterActivator.class);
    private ThreadManager threadManager;

    @Override
    public void start() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("[ThreadManagerActivator][startBundle] : Thread manager bundle is being started ...");
        }
        this.getService(IAdministrationService.class);
        this.threadManager = new ThreadManager(this.services);
        this.registerService(IExecuterManager.class, this.threadManager);
    }

    @Override
    public void stop() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("[ThreadManagerActivator][startBundle] : Thread manager bundle is being stopped ...");
        }
    }

}
