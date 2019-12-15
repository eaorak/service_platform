package com.adenon.sp.monitoring.counters;

public enum CounterType {

    REGULAR,
    TPS;

    static {
        StringBuilder s = new StringBuilder();
        for (CounterType t : values()) {
            s.append(t.name() + " ");
        }
        str = s.toString();
    }

    private static String str;

    public static String choices() {
        return str;
    }

}
