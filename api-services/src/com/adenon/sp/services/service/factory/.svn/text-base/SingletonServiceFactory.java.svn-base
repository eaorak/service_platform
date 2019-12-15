package com.adenon.sp.services.service.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonServiceFactory implements IServiceFactory {

    private Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<Class<?>, Object>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getServiceInstance(Class<T> srvClass) throws Exception {
        Object instance = this.instanceMap.get(srvClass);
        if (instance == null) {
            synchronized (srvClass) {
                if (instance == null) {
                    instance = srvClass.newInstance();
                    this.instanceMap.put(srvClass, instance);
                }
            }
        }
        return (T) instance;
    }

}
