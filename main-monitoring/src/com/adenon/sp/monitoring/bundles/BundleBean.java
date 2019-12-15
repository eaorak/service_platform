package com.adenon.sp.monitoring.bundles;

import org.apache.log4j.Level;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.osgi.ManifestHeaders;


@MBean(location = ConfigLocation.SYSTEM, parent = BundleMonitor.class, id = "name")
public class BundleBean {

    private BundleInfo bundleInfo;

    public BundleBean(BundleInfo bundleInfo) {
        this.bundleInfo = bundleInfo;
    }

    @Deprecated
    public BundleBean() {
    }

    @Attribute(readOnly = true, name = "Name")
    public String getName() {
        return this.bundleInfo.get(ManifestHeaders.BUNDLE_NAME);
    }

    @Attribute(readOnly = true, name = "Symbolic name")
    public String getSymbolicName() {
        return this.bundleInfo.get(ManifestHeaders.BUNDLE_SYMBOLICNAME);
    }

    @Attribute(readOnly = true, name = "Version")
    public String getVersion() {
        return this.bundleInfo.get(ManifestHeaders.BUNDLE_VERSION);
    }

    @Attribute(readOnly = true, name = "Vendor")
    public String getVendor() {
        return this.bundleInfo.get(ManifestHeaders.BUNDLE_VENDOR);
    }

    @Attribute(name = "Log level")
    public String getLogLevel() {
        return this.bundleInfo.getLogInfo().getLevel().toString();
    }

    public void setLogLevel(String levelStr) {
        Level level = Level.toLevel(levelStr);
        this.bundleInfo.setBundleLogLevel(level);
    }

}
