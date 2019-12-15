package com.adenon.sp.executer.pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.adenon.sp.executer.config.TaskRuntimeInfo;


public class TFuture<V> implements Future<V> {

    protected Future<V>       future;
    protected TaskRuntimeInfo info;

    public TFuture(Future<V> future,
                   TaskRuntimeInfo info) {
        this.future = future;
        this.info = info;
    }

    @SuppressWarnings("unchecked")
    public void injectFuture(Future<?> future) {
        this.future = (Future<V>) future;
    }

    @Override
    public boolean cancel(boolean interrupt) {
        return this.cancel(interrupt, true);
    }

    public boolean cancel(boolean interrupt,
                          boolean deleteConfiguration) {
        boolean cancelled = this.future.cancel(interrupt);
        return cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.future.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.future.get();
    }

    @Override
    public V get(long timeout,
                 TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.future.get(timeout, unit);
    }

}
