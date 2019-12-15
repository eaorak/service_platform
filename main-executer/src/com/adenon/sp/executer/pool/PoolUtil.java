package com.adenon.sp.executer.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adenon.sp.executer.task.pool.IThreadPool;


public class PoolUtil<T extends IThreadPoolInternal> {

    private Map<String, RegularPoolProxy<T>> proxies = new ConcurrentHashMap<String, RegularPoolProxy<T>>();
    private T                                pool;

    public PoolUtil(T pool) {
        this.pool = pool;
    }

    @SuppressWarnings("unchecked")
    public synchronized <P extends IThreadPool> P createOrGetProxy(Class<P> clazz,
                                                                   String bundleName) {
        RegularPoolProxy<T> proxy = this.proxies.get(bundleName);
        if (proxy == null) {
            proxy = (RegularPoolProxy<T>) this.pool.createProxy(bundleName);
            this.proxies.put(bundleName, proxy);
        }
        return (P) proxy;
    }

}
