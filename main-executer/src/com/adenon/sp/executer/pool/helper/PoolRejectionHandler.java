package com.adenon.sp.executer.pool.helper;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

public class PoolRejectionHandler implements RejectedExecutionHandler {

    private static Logger logger = Logger.getLogger(PoolRejectionHandler.class);

    @Override
    public void rejectedExecution(Runnable r,
                                  ThreadPoolExecutor executor) {
        // TODO
        logger.error("[PoolRejectionHandler][rejectedExecution] : Execution of task : " + r + " has been rejected in pool " + executor);
    }

}
