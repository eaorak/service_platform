package com.adenon.sp.monitoring.bundles;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.adenon.sp.administration.AdministrationException;
import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.utils.log.IBundleListener;
import com.adenon.sp.kernel.utils.log.ILoggingService;


@MBean(location = ConfigLocation.SYSTEM, subLocation = "monitoring=Monitoring,bundles=Bundles")
public class BundleMonitor implements IBundleListener {

    private static final Logger               logger  = Logger.getLogger(BundleMonitor.class);
    private final Map<BundleInfo, BundleBean> beanMap = new ConcurrentHashMap<BundleInfo, BundleBean>();
    private IAdministrationService            administration;
    private ILoggingService                   logging;

    public BundleMonitor() {
    }

    public void initialize(IAdministrationService administration,
                           ILoggingService logging,
                           List<BundleInfo> infoList) throws AdministrationException {
        this.administration = administration;
        this.logging = logging;
        administration.registerBean(this);
        for (BundleInfo info : infoList) {
            this.registered(info);
        }
    }

    @Override
    public void registered(BundleInfo info) {
        BundleBean bundleBean = new BundleBean(info);
        try {
            this.administration.registerBean(bundleBean);
            this.beanMap.put(info, bundleBean);
        } catch (AdministrationException e) {
            logger.error("Error while registering bundle info [" + info + "] : " + e.getMessage(), e);
        }
    }

    @Override
    public void unregistered(BundleInfo info) {
        BundleBean bundleBean = this.beanMap.remove(info);
        if (bundleBean == null) {
            return;
        }
        try {
            this.administration.unregisterBean(bundleBean);
        } catch (AdministrationException e) {
            logger.error("Error while unregistering bundle info [" + info + "] : " + e.getMessage(), e);
        }
    }

    @Operation(name = "Change system log level", desc = "Change the log levels of all bundles.")
    public void changeLogLevels(@Parameter(name = "Log level") String levelStr) {
        Level level = Level.toLevel(levelStr);
        if (level == null) {
            throw new InvalidParameterException("No level exist with name [" + levelStr + "] !");
        }
        this.logging.setSystemLogLevel(level);
    }

    @Operation(name = "Show system log level", desc = "Show the system log level.")
    public String getSystemLogLevel() {
        return this.logging.getSystemLogLevel().toString();
    }


}
