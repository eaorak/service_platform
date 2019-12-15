package com.adenon.sp.service.test;

import com.adenon.sp.channel.test.api.ITestChannelProvider;
import com.adenon.sp.services.service.factory.IServiceFactory;


public class TestServiceFactory implements IServiceFactory {

    private TestService testService;

    public TestServiceFactory(ITestChannelProvider api) {
        this.testService = new TestService(api);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getServiceInstance(Class<T> srvClass) throws Exception {
        if (srvClass == TestService.class) {
            return (T) this.testService;
        }
        throw new RuntimeException("No service exist named '" + srvClass.getName() + "' !");
    }
}
