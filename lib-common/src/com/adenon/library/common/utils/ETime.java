package com.adenon.library.common.utils;


public enum ETime {
    YEAR {

        @Override
        public long getMiliseconds(long unit) {
            return YEARS * unit;
        }

        @Override
        public long getMiliseconds() {
            return YEARS;
        }

    },
    MONTH {

        @Override
        public long getMiliseconds(long unit) {
            return MONTHS * unit;
        }

        @Override
        public long getMiliseconds() {
            return MONTHS;
        }

    },
    DAY {

        @Override
        public long getMiliseconds(long unit) {
            return DAYS * unit;
        }

        @Override
        public long getMiliseconds() {
            return DAYS;
        }

    },
    HOUR {

        @Override
        public long getMiliseconds(long unit) {
            return HOURS * unit;
        }

        @Override
        public long getMiliseconds() {
            return HOURS;
        }

    },
    MINUTE {

        @Override
        public long getMiliseconds(long unit) {
            return MINUTES * unit;
        }

        @Override
        public long getMiliseconds() {
            return MINUTES;
        }


    },
    SECOND {

        @Override
        public long getMiliseconds(long unit) {
            return SECONDS * unit;
        }

        @Override
        public long getMiliseconds() {
            return SECONDS;
        }

    };

    private static final long MILISECONDS = 1;
    private static final long SECONDS     = 1000 * MILISECONDS;
    private static final long MINUTES     = 60 * SECONDS;
    private static final long HOURS       = 60 * MINUTES;
    private static final long DAYS        = 24 * HOURS;
    private static final long MONTHS      = 30 * DAYS;
    private static final long YEARS       = 12 * MONTHS;

    public long getMiliseconds(long unit) {
        throw new AbstractMethodError();
    }

    public long getMiliseconds() {
        throw new AbstractMethodError();
    }

    public long getRemainingMiliseconds(long miliseconds) {
        return miliseconds % this.getMiliseconds();
    }

    public int getUnit(long miliseconds) {
        if (miliseconds < this.getMiliseconds()) {
            return 0;
        }
        return (int) (miliseconds / this.getMiliseconds());
    }

    public String convertToString(int unit) {
        StringBuilder strBuild = new StringBuilder(6);
        if (unit < 10) {
            strBuild.append("0");
        }
        strBuild.append(unit);
        return strBuild.toString();
    }

    public void convertToString(int unit,
                                StringBuilder strBuild) {
        if (unit < 10) {
            strBuild.append("0");
        }
        strBuild.append(unit);
    }

}
