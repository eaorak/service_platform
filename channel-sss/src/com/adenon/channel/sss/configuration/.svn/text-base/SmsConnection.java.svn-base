package com.adenon.channel.sss.configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;


@MBean(parent = SmsConnectionGroup.class, persist = true, id = "connectionName")
public class SmsConnection {

    private String  connectionName;
    private String  groupName;
    private String  username;
    private String  password;
    private int     port;
    private String  ipList;
    private int     smppWindowSize;
    private int     connectionType;
    private boolean trace          = false;
    private int     maxThreadCount = 5;

    public SmsConnection() {
    }

    @Attribute
    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(final String connectionName) {
        this.connectionName = connectionName;
    }

    @Join
    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    @Attribute
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Attribute
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Attribute
    @Min(1024)
    @Max(9999)
    public int getPort() {
        return this.port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    @Attribute
    public String getIpList() {
        return this.ipList;
    }

    public void setIpList(final String ipList) {
        this.ipList = ipList;
    }

    @Attribute
    @NotNull
    public int getSmppWindowSize() {
        return this.smppWindowSize;
    }

    public void setSmppWindowSize(final int smppWindowSize) {
        this.smppWindowSize = smppWindowSize;
    }

    @Attribute
    public int getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(final int connectionType) {
        this.connectionType = connectionType;
    }

    @Attribute
    public boolean getTrace() {
        return this.trace;
    }

    public void setTrace(final boolean trace) {
        this.trace = trace;
    }

    @Attribute
    public int getMaxThreadCount() {
        return this.maxThreadCount;
    }

    public void setMaxThreadCount(final int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

}
