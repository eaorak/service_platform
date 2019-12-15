package com.adenon.api.smpp;

import org.apache.log4j.Level;

import com.adenon.api.smpp.core.SmppApiDelegator;
import com.adenon.api.smpp.sdk.LogType;


public class SmppLoggerApi {

    private SmppApiDelegator smppApiDelegator;

    public SmppLoggerApi(SmppApiDelegator smppApiDelegator) {
        this.smppApiDelegator = smppApiDelegator;
    }

    public void changeLogType(LogType pLogtype) {
        this.smppApiDelegator.getSmppLoggingManager().changeLogType(pLogtype);
    }

    public void changeLogLevel(Level level) {
        this.smppApiDelegator.getSmppLoggingManager().changeLogLevel(level);
    }

}
