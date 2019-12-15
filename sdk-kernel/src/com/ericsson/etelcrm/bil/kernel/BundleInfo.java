package com.ericsson.etelcrm.bil.kernel;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.framework.Bundle;


public class BundleInfo {

    private final String[] headers = new String[ManifestHeaders.values().length];
    private final int      bundleId;

    public static BundleInfo create(Bundle bundle) throws IOException {
        BundleInfo bundleInfo = new BundleInfo(bundle);
        return bundleInfo;
    }

    private BundleInfo(Bundle bundle) throws IOException {
        Dictionary<?, ?> bundleHeaders = bundle.getHeaders();
        for (ManifestHeaders header : ManifestHeaders.values()) {
            String value = (String) bundleHeaders.get(header.getHeader());
            this.put(header, value);
        }
        this.bundleId = (int) bundle.getBundleId();
    }

    public String get(ManifestHeaders header) {
        return this.headers[header.ordinal()];
    }

    public void put(ManifestHeaders header,
                    String value) {
        this.headers[header.ordinal()] = value;
    }

    public int getBundleId() {
        return this.bundleId;
    }

    @Override
    public String toString() {
        return "BundleInfo [name="
               + this.get(ManifestHeaders.BUNDLE_NAME)
               + ", sym-name="
               + this.get(ManifestHeaders.BUNDLE_SYMBOLICNAME)
               + ", version="
               + this.get(ManifestHeaders.BUNDLE_VERSION)
               + "]";
    }

}