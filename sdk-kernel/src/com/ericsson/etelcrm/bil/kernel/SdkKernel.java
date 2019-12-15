package com.ericsson.etelcrm.bil.kernel;

import org.apache.log4j.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public abstract class SdkKernel implements BundleActivator {

    protected Services      services;
    protected BundleInfo    bundleInfo;
    protected String        confPath;
    protected BundleContext context;
    protected Logger        logger;

    public SdkKernel() {
        this.logger = Logger.getLogger(this.getClass());
    }

    @Override
    public final void start(final BundleContext context) throws Exception {
        this.context = context;
        this.bundleInfo = BundleInfo.create(context.getBundle());
        this.services = new Services(this.bundleInfo);
        try {
            this.start();
        } catch (final Throwable e) {
            this.logger.error("Error on startup ! " + e.getMessage(), e);
            System.err.println("Error on essential bundle : " + e.getMessage());
            this.exit();
        }
    }

    @Override
    public final void stop(final BundleContext context) throws Exception {
        this.logger.info(">> Stopping Bundle :: " + context.getBundle().getSymbolicName());
        this.stop();
        this.context = null;
    }

    public final <T> T getService(final Class<T> serviceClass) {
        T service = this.getServiceInternal(serviceClass);
        this.services.addService(serviceClass, service);
        return service;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T getServiceInternal(final Class<T> serviceClass) {
        T service = null;
        final ServiceReference srvRef = this.context.getServiceReference(serviceClass.getName());
        if ((srvRef == null) || ((service = (T) this.context.getService(srvRef)) == null)) {
            throw new RuntimeException("Error ! Could not load '" + serviceClass.getName() + "' service !");
        }
        return service;
    }

    public final Services getServices(final Class<?>... serviceClasses) {
        for (final Class<?> c : serviceClasses) {
            this.getService(c);
        }
        return this.services;
    }

    protected <T> T registerService(final Class<T> inf,
                                    final T service) {
        return this.registerService(inf, service, true);
    }

    @SuppressWarnings("unchecked")
    protected <T> T registerService(final Class<T> inf,
                                    final T service,
                                    boolean proxy) {
        this.context.registerService(inf.getName(), service, null);
        return service;
    }

    private void exit() {
        String error = "ESSENTIAL BUNDLE [" + this.bundleInfo.get(ManifestHeaders.BUNDLE_SYMBOLICNAME) + "] COULD NOT BE STARTED ! SHUTTING DOWN SYSTEM !";
        System.err.println(error);
        String warn = "*********************************************************************************";
        this.logger.fatal(warn);
        this.logger.fatal(error);
        this.logger.fatal(warn);
        System.exit(1);
    }

    public Bundle getBundle() {
        return this.context.getBundle();
    }

    public BundleInfo getBundleInfo() {
        return this.bundleInfo;
    }

    //

    protected abstract void start() throws Exception;

    protected abstract void stop() throws Exception;

}
