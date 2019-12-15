package com.adenon.sp.executer.task;

import com.adenon.sp.executer.task.ResultType;


public class Return<T> implements IReturn<T> {

    private T          returnObj;
    private ResultType type;
    private long       delta;
    private String     description;

    public Return(T result,
                  ResultType type,
                  long delta,
                  String description) {
        this.returnObj = result;
        this.type = type;
        this.delta = delta;
        this.description = description;
    }

    @Override
    public T result() {
        return this.returnObj;
    }

    @Override
    public ResultType resultType() {
        return this.type;
    }

    @Override
    public long delta() {
        return this.delta;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String toString() {
        return "[Process :: Type:" + this.type + " Delta:" + this.delta + " Desc:" + this.description + "]";
    }

}
