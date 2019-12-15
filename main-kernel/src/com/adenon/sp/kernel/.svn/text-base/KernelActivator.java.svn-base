package com.adenon.sp.kernel;

import java.util.HashMap;
import java.util.Map;

import com.adenon.sp.kernel.codegen.AssistService;
import com.adenon.sp.kernel.dialog.Dialog;
import com.adenon.sp.kernel.dialog.IDialogCache;
import com.adenon.sp.kernel.dialog.internal.DialogCache;
import com.adenon.sp.kernel.osgi.IOsgiUtilService;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;
import com.adenon.sp.kernel.services.BundleInfoService;
import com.adenon.sp.kernel.services.FrameworkServices;
import com.adenon.sp.kernel.services.IAssistService;
import com.adenon.sp.kernel.services.IBundleInfoService;
import com.adenon.sp.kernel.services.OsgiUtilService;
import com.adenon.sp.kernel.utils.IBundleScanService;
import com.adenon.sp.kernel.utils.IFrameworkServices;
import com.adenon.sp.kernel.utils.BundleScanService;

public class KernelActivator extends BundleActivator {

    private final Map<Class<?>, Object> kernelServices = new HashMap<Class<?>, Object>();

    @Override
    public void start() throws Exception {
        // ------------ Code generation [ ** Should be the first ** ] ------------
        this.registerService(IAssistService.class, AssistService.getInstance());
        // ------------------------------------------------------------------------
        final BundleScanService classUtilInstance = BundleScanService.getInstance();
        try {
            // this.getService(IBundleClassService.class);
            // Service is registered by osgi fragment in this case.
        } catch (final Exception e) {
            // We will register our implementation if not registered.
        }
        //
        this.registerService(IBundleInfoService.class, BundleInfoService.getInstance());
        this.registerService(IOsgiUtilService.class, OsgiUtilService.getInstance());
        this.registerService(IBundleScanService.class, classUtilInstance, false);
        //
        final DialogCache dialogCache = DialogCache.getInstance();
        Dialog.initialize(0, dialogCache); // TODO : Node Id
        this.registerService(IDialogCache.class, dialogCache);
        // ----------- Kernel Services - * Should be the last * -------------------
        super.registerService(IFrameworkServices.class, new FrameworkServices(this.kernelServices));
        this.logger.debug("Core has started ...");
        // ------------------------------------------------------------------------
    }

    @Override
    protected <T> T registerService(final Class<T> inf, final T service) {
        return this.registerService(inf, service, true);
    }

    @Override
    protected <T> T registerService(final Class<T> inf, final T service, final boolean proxy) {
        final T srv = super.registerService(inf, service, proxy);
        this.kernelServices.put(inf, srv);
        return service;
    }

    @Override
    public void stop() throws Exception {
    }
}
