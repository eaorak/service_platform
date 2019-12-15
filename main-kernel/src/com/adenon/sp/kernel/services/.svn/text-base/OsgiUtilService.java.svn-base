package com.adenon.sp.kernel.services;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.adenon.sp.kernel.osgi.IOsgiUtilService;

public class OsgiUtilService implements IOsgiUtilService {

    private static final OsgiUtilService INSTANCE = new OsgiUtilService();

    public static OsgiUtilService getInstance() {
        return INSTANCE;
    }

    private OsgiUtilService() {
    }

    @Override
    public Bundle findBundle(final BundleContext context, final String symbolicName) {
        for (final Bundle bundle : context.getBundles()) {
            if (bundle.getSymbolicName().equals(symbolicName)) {
                return bundle;
            }
        }
        return null;
    }

}
