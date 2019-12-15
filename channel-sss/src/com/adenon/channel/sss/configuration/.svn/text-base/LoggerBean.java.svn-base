package com.adenon.channel.sss.configuration;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;

@MBean(location = ConfigLocation.SYSTEM, subLocation = "LopParameters=Log Paramaters", persist = true)
public class LoggerBean {

    private int     logType;
    private int     logLevel;
    private boolean writeConsole;

    @Attribute
    public int getLogType() {
        return this.logType;
    }

    public void setLogType(final int logType) {
        this.logType = logType;
    }

    @Attribute
    public int getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(final int logLevel) {
        this.logLevel = logLevel;
    }

    @Attribute
    public boolean getWriteConsole() {
        return this.writeConsole;
    }

    public void setWriteConsole(final boolean writeConsole) {
        this.writeConsole = writeConsole;
    }
}
