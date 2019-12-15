package com.adenon.sp.executer.task.cron;

public interface ICronJob {

    String getCronExpression();

    boolean interrupt() throws Exception;

}
