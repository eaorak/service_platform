package com.adenon.sp.persistence;

import com.adenon.sp.kernel.utils.IKeyEnum;

public enum SystemPool implements IKeyEnum {

    SYSTEM("system"),
    STATS("stats");

    private String key;

    private SystemPool(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return this.key;
    }

    public static boolean isSystemPool(String poolName) {
        for (SystemPool p : SystemPool.values()) {
            if (p.key().equals(poolName)) {
                return true;
            }
        }
        return false;
    }

}