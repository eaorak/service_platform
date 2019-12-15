package com.adenon.sp.executer.pool;

public enum TaskPriority {

    VERY_LOW(0),
    LOW(2),
    NORMAL(5),
    HIGH(8),
    VERY_HIGH(10);

    private int priority;

    private TaskPriority(int priority) {
        this.priority = priority;
    }

    public int asNumber() {
        return this.priority;
    }

}
