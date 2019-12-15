package com.adenon.sp.executer.task;

public interface IRuntimeInfo {

    long increaseRun(long runDelta,
                     boolean success);

    String getTaskName();

    long getRunCount();

    long getSuccessCount();

    long getFailureCount();

    long getAverageTime();

}
