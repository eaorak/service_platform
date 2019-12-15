package com.adenon.sp.service.test;

import com.adenon.sp.channel.test.api.ITestChannelProvider;
import com.adenon.sp.services.service.ServiceActivator;
import com.adenon.sp.services.service.factory.IServiceFactory;


public class TestServiceActivator extends ServiceActivator {

    private IServiceFactory factory;

    @Override
    public void startService() throws Exception {
        ITestChannelProvider apiService = this.getService(ITestChannelProvider.class);
        this.factory = new TestServiceFactory(apiService);
    }

    @Override
    public void stopService() throws Exception {
    }

    @Override
    protected IServiceFactory getServiceFactory() {
        return this.factory;
    }

}
