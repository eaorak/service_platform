package com.adenon.sp.executer.task;

import java.util.concurrent.Callable;

import com.adenon.sp.executer.task.ResultType;

public class TCallable<T> implements Callable<Return<T>> {

    private Callable<T> callable;

    public TCallable(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public Return<T> call() throws Exception {
        long startTime = System.currentTimeMillis();
        T returnObj = null;
        String errorMessage = null;
        Return<T> retObj = null;
        try {
            returnObj = this.callable.call();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            throw e;
        } finally {
            long delta = System.currentTimeMillis() - startTime;
            ResultType rtype = errorMessage == null ? ResultType.SUCCESS : ResultType.FAIL;
            retObj = new Return<T>(returnObj, rtype, delta, this.getDescription(rtype, errorMessage));
        }
        return retObj;
    }

    private String getDescription(ResultType rtype,
                                  String message) {
        switch (rtype) {
            case SUCCESS:
                return "Process completed successfully";
            case FAIL:
                return "Error occured : [" + message + "]";
            default:
                return "Message : " + message;
        }
    }
}
