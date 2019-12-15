package com.adenon.sp.kernel.services;

import java.util.Map;

import com.adenon.sp.kernel.osgi.IOsgiUtilService;
import com.adenon.sp.kernel.utils.IBundleScanService;
import com.adenon.sp.kernel.utils.IFrameworkServices;
import com.adenon.sp.kernel.utils.log.ILoggingService;
import com.adenon.sp.kernel.utils.log.LoggingService;

public class FrameworkServices implements IFrameworkServices {

    private final Map<Class<?>, Object> services;
    //
    // private final IBundleClassService classUtilService;
    private final ILoggingService       loggingService;
    private final IOsgiUtilService      osgiUtilService;
    private final IBundleInfoService    bundleInfoService;
    private final IAssistService        assistService;
    private final IBundleScanService    scannerService;

    public FrameworkServices(final Map<Class<?>, Object> services) {
        this.services = services;
        this.loggingService = LoggingService.getInstance();
        this.assistService = this.get(IAssistService.class);
        // this.classUtilService = this.get(IBundleClassService.class);
        this.bundleInfoService = this.get(IBundleInfoService.class);
        this.osgiUtilService = this.get(IOsgiUtilService.class);
        this.scannerService = this.get(IBundleScanService.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T get(final Class<T> inf) {
        return (T) this.services.get(inf);
    }

    @Override
    public ILoggingService loggingService() {
        return this.loggingService;
    }

    // @Override
    // public IBundleClassService classUtilService() {
    // return this.classUtilService;
    // }

    @Override
    public IOsgiUtilService osgiUtilService() {
        return this.osgiUtilService;
    }

    @Override
    public IBundleInfoService bundleInfoService() {
        return this.bundleInfoService;
    }

    @Override
    public IAssistService assistService() {
        return this.assistService;
    }

    @Override
    public IBundleScanService bundleScanner() {
        return this.scannerService;
    }

}
