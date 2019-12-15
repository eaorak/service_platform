package com.adenon.smpp.server.managers;

import java.util.ArrayList;

import org.apache.log4j.Level;

import com.adenon.api.smpp.core.IIOReactor;
import com.adenon.api.smpp.logging.LogController;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.sdk.LogDescriptor;
import com.adenon.api.smpp.sdk.LogType;
import com.adenon.smpp.server.core.ServerApiDelegator;
import com.adenon.smpp.server.core.ServerIOReactor;

public class ServerLogManager {

    private LoggerWrapper      logger;
    private LogType            logType = LogType.LogAllInOneFile;
    private LogController      logControler;
    private Level              rootlevel;
    private String             apiEngineName;
    private ServerApiDelegator smppApiDelegator;
    private Object             syncObject;

    public ServerLogManager(String name,
                            LogDescriptor descriptor,
                            ServerApiDelegator smppApiDelegator,
                            Object pSyncObject) {
        this.syncObject = pSyncObject;
        this.smppApiDelegator = smppApiDelegator;
        this.apiEngineName = name;
        this.logControler = new LogController(this.apiEngineName, descriptor);
        this.getLogControler().initiliaze();
        this.logger = this.getLogControler().getLogger();

        if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("ServerLogManager", "ServerLogManager", 0, null, "LogManager initiated. System name : " + this.apiEngineName);
        }

    }

    public void changeLogLevel(Level level) {
        synchronized (this.syncObject) {
            if (this.getLogger().isInfoEnabled()) {
                this.getLogger().info("ServerLogManager", "changeLogLevel", 0, null, "Changing logging level for all ");
            }

            ArrayList<IIOReactor> ioReactors = this.smppApiDelegator.getSmppIOReactorStorage().getIoReactors();

            for (IIOReactor iioReactor : ioReactors) {
                ServerIOReactor ioReactor = (ServerIOReactor) iioReactor;
                if (ioReactor.getLogger() != null) {
                    ioReactor.getLogger().setLevel(level);
                    if (this.getLogger().isInfoEnabled()) {
                        this.getLogger().info("ServerLogManager", "changeLogLevel", 0, ioReactor.getLabel(), "Log level changed. level : " + level.toString());
                    }
                }
            }
        }
    }

    public void changeLogType(LogType pLogType) {
        if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("ServerLogManager", "changeLogType", 0, null, "Changing log style to : " + pLogType + " for all");
        }
        synchronized (this.syncObject) {
            ArrayList<IIOReactor> ioReactors = this.smppApiDelegator.getSmppIOReactorStorage().getIoReactors();

            for (IIOReactor iioReactor : ioReactors) {
                ServerIOReactor ioReactor = (ServerIOReactor) iioReactor;
                switch (pLogType) {
                    case LogAllInOneFile:
                        iioReactor.setLogger(this.logControler.getLogger());
                        break;
                    case LogConnectionGroupSeparetly:
                        iioReactor.setLogger(this.logControler.getLogger(ioReactor.getServerName()));
                        break;
                    case LogConnectionsSeparetly:
                        iioReactor.setLogger(this.logControler.getLogger(iioReactor.getLabel()));
                        break;
                    default:
                        break;
                }

                if (this.getLogger().isInfoEnabled()) {
                    this.getLogger().info("LogManager",
                                          "changeLogType",
                                          0,
                                          iioReactor.getLabel(),
                                          "Log style changed to : " + pLogType + " for : " + iioReactor.getLabel());
                }
            }
        }
        this.logType = pLogType;
    }

    public LogType getLogType() {
        return this.logType;
    }

    public void setLogType(LogType pLogType) {
        this.logType = pLogType;
    }

    public LogController getLogControler() {
        return this.logControler;
    }

    public LoggerWrapper getLogger() {
        return this.logger;
    }

    public Level getRootlevel() {
        return this.rootlevel;
    }

    public void setRootlevel(Level rootlevel) {
        this.rootlevel = rootlevel;
    }

}
