package com.adenon.sp.executer;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.executer.task.pool.IThreadPoolCore;


public interface IExecuterManagerInternal extends IExecuterManager {

    IThreadPoolCore getDefaultPool();

    IScheduledPool getScheduledPool();

    IThreadPoolCore getPool(String poolName);

    IAdministrationService getConfiguration();

}
