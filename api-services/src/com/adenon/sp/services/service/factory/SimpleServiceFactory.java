package com.adenon.sp.services.service.factory;

public class SimpleServiceFactory implements IServiceFactory {

    @Override
    public <T> T getServiceInstance(Class<T> srvClass) throws Exception {
        return srvClass.newInstance();
    }

}
