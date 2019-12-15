package com.adenon.sp.executer;

import java.util.concurrent.RejectedExecutionHandler;

public interface IPoolConfig {

    String getBundleName();

    String getPoolName();

    int getCoreSize();

    IPoolConfig setCoreSize(int coreSize);

    int getMaxSize();

    IPoolConfig setMaxSize(int maxSize);

    RejectedExecutionHandler getRejectHandler();

    IPoolConfig setRejectHandler(RejectedExecutionHandler rejectHandler);

}