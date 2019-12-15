package com.adenon.smpp.server.api;

import org.apache.log4j.Level;

import com.adenon.api.smpp.sdk.LogType;
import com.adenon.smpp.server.core.ServerApiDelegator;


public class ServerLoggerApi {

    private final ServerApiDelegator serverApiDelegator;

    public ServerLoggerApi(ServerApiDelegator serverApiDelegator) {
        this.serverApiDelegator = serverApiDelegator;
    }

    public void changeLogLevel(Level level) {
        this.serverApiDelegator.getLogManager().changeLogLevel(level);
    }

    public void changeLogType(LogType pLogType) {
        this.serverApiDelegator.getLogManager().changeLogType(pLogType);
    }
}
