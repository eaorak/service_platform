package com.adenon.channel.sms.configuration.beans;

import org.apache.log4j.Level;

import com.adenon.api.smpp.SmppLoggerApi;
import com.adenon.api.smpp.sdk.LogType;
import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;


@MBean(location = ConfigLocation.CHANNELS, subLocation = "SmsAdapter=SMS,logging=Log", persist = true)
public class LoggerBean {

    private static final int LOG_LEVEL_DEBUG = 0;
    private static final int LOG_LEVEL_INFO  = 1;
    private static final int LOG_LEVEL_WARN  = 2;
    private static final int LOG_LEVEL_ERROR = 3;

    private int              logType;
    private int              logLevel;
    private boolean          writeConsole;
    private SmppLoggerApi    smppLoggerApi;


    public LoggerBean() {
    }

    public LoggerBean(SmppLoggerApi smppLoggerApi) {
        this.setSmppLoggerApi(smppLoggerApi);
    }

    @Attribute(description = "0-Use only one file 1-Use separate files for groups 2-Use separate files for connections")
    public int getLogType() {
        return this.logType;
    }

    public void setLogType(final int logType) {
        if (this.smppLoggerApi != null) {
            LogType logTypeExp = LogType.getLogType(logType);
            this.getSmppLoggerApi().changeLogType(logTypeExp);
        }
        this.logType = logType;
    }

    @Attribute(description = "0-DEBUG 1-INFO 2-WARN 3-ERROR")
    public int getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(final int logLevel) {
        Level log4jLevel = Level.INFO;
        if (this.smppLoggerApi != null) {
            log4jLevel = this.getLog4JLevel(logLevel);
            this.getSmppLoggerApi().changeLogLevel(log4jLevel);
        }
        this.logLevel = this.convertLog4jLevel(log4jLevel);
    }

    public Level getLog4JLevel(int logLevel) {
        Level myLevel = Level.INFO;
        switch (logLevel) {
            case LOG_LEVEL_DEBUG:
                myLevel = Level.DEBUG;
                break;
            case LOG_LEVEL_INFO:
                myLevel = Level.INFO;
                break;
            case LOG_LEVEL_WARN:
                myLevel = Level.WARN;
                break;
            case LOG_LEVEL_ERROR:
                myLevel = Level.ERROR;
                break;
            default:
                myLevel = Level.INFO;
                break;
        }
        return myLevel;
    }

    public int convertLog4jLevel(Level level) {
        switch (level.toInt()) {
            case Level.DEBUG_INT:
                return LOG_LEVEL_DEBUG;
            case Level.INFO_INT:
                return LOG_LEVEL_INFO;
            case Level.WARN_INT:
                return LOG_LEVEL_WARN;
            case Level.ERROR_INT:
                return LOG_LEVEL_ERROR;
            default:
                return LOG_LEVEL_INFO;
        }
    }

    @Attribute
    public boolean getWriteConsole() {
        return this.writeConsole;
    }

    public void setWriteConsole(final boolean writeConsole) {
        this.writeConsole = writeConsole;
    }

    public SmppLoggerApi getSmppLoggerApi() {
        return this.smppLoggerApi;
    }

    public void setSmppLoggerApi(SmppLoggerApi smppLoggerApi) {
        this.smppLoggerApi = smppLoggerApi;
    }
}
