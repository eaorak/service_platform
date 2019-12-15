package com.adenon.api.smpp.sdk;

import com.adenon.api.smpp.common.State;
import com.adenon.api.smpp.core.IIOReactor;

public class ConnectionInformation {

    private final IIOReactor smppIOReactor;
    private String           connectionName;
    private String           connectionGroupName;
    private String           ip;
    private int              port;
    private boolean          connected;
    private State            state = new State();
    private String           connectionLabel;
    private String           userName;


    public ConnectionInformation(final IIOReactor smppIOReactor,
                                 final String connectionGroupName,
                                 final String connectionName) {
        this.smppIOReactor = smppIOReactor;
        this.connectionGroupName = connectionGroupName;
        this.setConnectionName(connectionName);
    }


    public String getConnectionName() {
        return this.connectionName;
    }


    public String getConnectionGroupName() {
        return this.connectionGroupName;
    }


    public String getIp() {
        return this.ip;
    }

    public ConnectionInformation setIp(final String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(final boolean connected) {
        this.connected = connected;
    }

    public State getConnectionState() {
        return this.state;
    }

    public void setConnectionState(final State light) {
        this.state = light;
    }

    public String getConnectionLabel() {
        return this.connectionLabel;
    }

    public void setConnectionLabel(final String connectionLabel) {
        this.connectionLabel = connectionLabel;
    }


    public IIOReactor getSmppIOReactor() {
        return this.smppIOReactor;
    }

    @Override
    public String toString() {
        return this.ip + ":" + this.port + " [" + this.connectionGroupName + "@" + this.getConnectionName() + "]";
    }


    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

}
