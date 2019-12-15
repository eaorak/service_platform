package com.adenon.sp.executer.task.cron;

public interface ICronPool {

    ICron createExpression();

    ICronJob schedule(String taskName,
                      ICron expression,
                      Runnable task) throws Exception;

}
