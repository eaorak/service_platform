package com.adenon.service.testsms;

import com.adenon.channel.sms.api.operation.ISmsSender;
import com.adenon.service.testsms.threadpool.ServiceRuntime;
import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.IPoolConfig;
import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.services.service.ServiceActivator;
import com.adenon.sp.services.service.factory.IServiceFactory;
import com.adenon.sp.services.service.factory.SingletonServiceFactory;


public class SmsTestActivator extends ServiceActivator {

    private static final String THREAD_POOL_NAME = "testsms";
    private IThreadPool         threadPool;
    private IExecuterManager    executerManager;

    @Override
    protected void startService() throws Exception {
        this.executerManager = this.getService(IExecuterManager.class);

        IPoolConfig poolConfig = this.executerManager.newConfig("SmsTestBundle", THREAD_POOL_NAME);

        this.threadPool = this.executerManager.createOrGetPool(poolConfig);

        ISmsSender serviceSmsSender = this.getService(ISmsSender.class);
        if (serviceSmsSender == null) {
            throw new RuntimeException("Send sms interface is Null");
        }
        ServiceRuntime serviceRuntime = new ServiceRuntime(this.threadPool, serviceSmsSender);
        serviceRuntime.start();

    }

    @Override
    protected void stopService() throws Exception {
        if (!this.executerManager.deletePool(THREAD_POOL_NAME)) {
            // TODO : throw exception
        }
    }

    @Override
    protected IServiceFactory getServiceFactory() {
        return new SingletonServiceFactory();
    }

    public IThreadPool getThreadPool() {
        return this.threadPool;
    }
}
