package com.adenon.sp.executer.task.cron;

public enum CronParts {

    MILLIS(10, 999, "0/", "0"), //
    SECOND(0, 59, ",-*/"), //
    MINUTE(0, 59, ",-*/"), //
    HOUR(0, 23, ",-*/"), //
    DAYOFMONTH(1, 31, ",-*?/LW"), //
    MONTH(1, 12, ",-*/"), //
    DAYOFWEEK(1, 7, ",-*?/L#", "?"), //
    YEAR(1970, 2099, ",-*/", "");

    private static CronParts[] stdParts;

    static {
        stdParts = new CronParts[CronParts.values().length - 1];
        int i = 0;
        for (CronParts part : CronParts.values()) {
            if (part == MILLIS) {
                continue;
            }
            stdParts[i++] = part;
        }
    }

    private int                min;
    private int                max;
    private String             values;
    private String             defaultVal;

    private CronParts(int min,
                      int max,
                      String values) {
        this(min, max, values, "*");
    }

    private CronParts(int min,
                      int max,
                      String values,
                      String defaultVal) {
        this.min = min;
        this.max = max;
        this.values = values;
        this.defaultVal = defaultVal;
    }

    public int min() {
        return this.min;
    }

    public int max() {
        return this.max;
    }

    public String defaultVal() {
        return this.defaultVal;
    }

    public String validValues() {
        return this.values;
    }

    public static CronParts[] stdParts() {
        return stdParts;
    }

}