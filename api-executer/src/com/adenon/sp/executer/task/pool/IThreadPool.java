package com.adenon.sp.executer.task.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface IThreadPool extends Executor, IPoolInfo {

    /**
     * Executes given task sometime in future.<br>
     * <br>
     * If the given task implements IRunnable or ICallable, then pool watchdog tracks <br>
     * executing tasks and finishes tasks that exceed given maximum execution time. <br>
     * 
     * If you want your tasks to be managed for maximum execution time, you should <br>
     * use either IRunnable or ICallable interfaces.
     * 
     * @param task Runnable or IRunnable for time managament
     */
    @Override
    void execute(Runnable task);

    /**
     * Submits given task for execution and returns Future for tracking and controlling <br>
     * given task.
     * 
     * @param task Runnable or IRunnable for time managament
     * @return Future
     * @see #execute
     */
    Future<?> submit(Runnable task);

    /**
     * Submits given task for execution and returns Future for tracking and controlling <br>
     * given task.
     * 
     * @param task Callable or ICallable for time managament
     * @return Future
     * @see #execute
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * Submits given task for execution and returns Future for tracking and controlling <br>
     * given task.
     * 
     * @param task Runnable or IRunnable for time managament
     * @return Future
     * @see #execute
     */
    <T> Future<T> submit(Runnable task,
                         T result);

}
