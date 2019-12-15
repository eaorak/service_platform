package com.adenon.sp.services.service.factory;

public interface IServiceFactory {

    <T> T getServiceInstance(Class<T> srvClass) throws Exception;

}
