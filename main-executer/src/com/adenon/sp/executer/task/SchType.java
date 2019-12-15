package com.adenon.sp.executer.task;

import java.util.Locale;

import com.adenon.sp.executer.config.ScheduleTaskRuntimeInfo;


public enum SchType {

    SCHEDULE,
    FIXED_RATE,
    FIXED_DELAY;

    public String toString(ScheduleTaskRuntimeInfo info) {
        StringBuilder str = new StringBuilder(this.name());
        String unit = "(" + info.getUnit().name().toLowerCase(Locale.ENGLISH) + ")";
        switch (this) {
            case SCHEDULE:
                str.append(":: Delay: ").append(info.getDelay()).append(" ").append(unit);
                break;
            case FIXED_RATE:
                str.append(":: Init.Delay: ").append(info.getInitialDelay()).append(" Period: ").append(info.getPeriod()).append(" ").append(unit);
                break;
            case FIXED_DELAY:
                str.append(":: Init.Delay: ").append(info.getInitialDelay()).append(" Delay: ").append(info.getDelay()).append(" ").append(unit);
                break;
        }
        return str.toString();
    }

}
