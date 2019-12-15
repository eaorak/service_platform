package com.adenon.sp.executer;

public enum DefaultPools {

    SYSTEM("#SP_POOL#"), //
    SCHEDULER("#SCH_POOL#");

    private String poolName;

    private DefaultPools(String poolName) {
        this.poolName = poolName;
    }

    public String poolName() {
        return this.poolName;
    }

    public static boolean canDelete(String poolName) {
        if ((poolName == null) || poolName.equals("")) {
            return false;
        }
        for (DefaultPools pool : DefaultPools.values()) {
            if (poolName.equals(pool.poolName)) {
                return false;
            }
        }
        return true;
    }

}
