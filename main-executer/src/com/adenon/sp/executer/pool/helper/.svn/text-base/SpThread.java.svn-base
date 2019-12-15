package com.adenon.sp.executer.pool.helper;

import com.adenon.sp.executer.task.pool.IManagedInternal;
import com.adenon.sp.executer.thread.IThread;

public class SpThread extends Thread implements IThread, Comparable<Integer> {

    private SpThreadFactory  factory;
    private int              index;
    private IManagedInternal currentTask;

    public SpThread(ThreadGroup group,
                    Runnable target,
                    String poolName,
                    int index,
                    SpThreadFactory factory) {
        super(group, target, poolName + "-" + index);
        this.index = index;
        this.factory = factory;
    }

    @Override
    public void run() {
        try {
            super.run();
        } finally {
            this.factory.remove(this);
        }
    }

    @Override
    public void interrupt() {
        try {
            if (this.currentTask != null) {
                this.currentTask.interrupting();
            }
        } finally {
            super.interrupt();
        }
    }

    @Override
    public IManagedInternal getCurrentTask() {
        return this.currentTask;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    public String getInfo() {
        return this.getName() + " Task: " + this.taskInfo();
    }

    private String taskInfo() {
        return this.currentTask == null ? "No task" : this.currentTask.getTaskClass();
    }

    public void setCurrentTask(IManagedInternal task) {
        this.currentTask = task;
    }

    @Override
    public int compareTo(Integer index) {
        return this.index - index;
    }

}
