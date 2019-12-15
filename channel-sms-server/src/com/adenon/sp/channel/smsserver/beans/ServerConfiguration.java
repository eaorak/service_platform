package com.adenon.sp.channel.smsserver.beans;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;


@MBean(parent = UsersBean.class, persist = true, id = "name")
public class ServerConfiguration {

    private String  name           = "server configuration";

    private int     threadCount    = 50;
    private int     port           = 5101;
    private int     windowSize     = 100;
    private int     maxThreadCount = 5;
    private boolean traceOn;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Attribute
    public int getThreadCount() {
        return this.threadCount;
    }


    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }


    @Attribute
    public int getPort() {
        return this.port;
    }


    public void setPort(int port) {
        this.port = port;
    }

    @Attribute
    public int getWindowSize() {
        return this.windowSize;
    }


    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }


    @Attribute
    public int getMaxThreadCount() {
        return this.maxThreadCount;
    }


    public void setMaxThreadCount(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    @Attribute
    public boolean getTraceOn() {
        return this.traceOn;
    }

    public void setTraceOn(boolean traceOn) {
        this.traceOn = traceOn;
    }

}
