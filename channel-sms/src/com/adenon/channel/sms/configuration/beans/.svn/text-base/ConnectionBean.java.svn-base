package com.adenon.channel.sms.configuration.beans;

import com.adenon.api.smpp.SmppConnectionApi;
import com.adenon.api.smpp.common.EState;
import com.adenon.api.smpp.common.State;
import com.adenon.api.smpp.sdk.ConnectionDescriptor;
import com.adenon.channel.sms.SmsActivator;
import com.adenon.channel.sms.configuration.mapper.ConnectionDescriptorMapper;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;


@MBean(parent = ConnectionGroupBean.class, persist = true, id = "connectionName")
public class ConnectionBean {

    private SmppConnectionApi connectionApi;
    private String            connectionGroupName;
    private String            connectionName;
    private String            username;
    private String            password;
    private int               port;
    private String            connectionIpList;
    private int               smppWindowSize;
    private int               connectionType;
    private boolean           trace            = false;
    private int               maxThreadCount   = 5;
    private boolean           connectionActive = false;
    private boolean           changesApllied   = true;

    private SmsActivator      smsActivator;

    public ConnectionBean() {
    }

    @Join
    public String getConnectionGroupName() {
        return this.connectionGroupName;
    }

    public void setConnectionGroupName(final String connectionGroupName) {
        this.connectionGroupName = connectionGroupName;
    }

    @Attribute(readOnly = true)
    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(final String connectionName) {
        this.connectionName = connectionName;
    }

    @Attribute
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
        this.setChangesApllied(false);
    }

    @Attribute
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
        this.setChangesApllied(false);
    }

    @Attribute
    public int getPort() {
        return this.port;
    }

    public void setPort(final int port) {
        this.port = port;
        this.setChangesApllied(false);
    }

    @Attribute
    public String getConnectionIpList() {
        return this.connectionIpList;
    }

    public void setConnectionIpList(final String ipList) {
        this.connectionIpList = ipList;
        this.setChangesApllied(false);
    }

    @Attribute
    public int getSmppWindowSize() {
        return this.smppWindowSize;
    }

    public void setSmppWindowSize(final int smppWindowSize) {
        this.smppWindowSize = smppWindowSize;
        this.setChangesApllied(false);
    }

    @Attribute
    public int getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(final int connectionType) {
        this.connectionType = connectionType;
        this.setChangesApllied(false);
    }

    @Attribute
    public boolean getTrace() {
        return this.trace;
    }

    public void setTrace(final boolean trace) {
        this.trace = trace;
        this.setChangesApllied(false);
    }

    @Attribute
    public int getMaxThreadCount() {
        return this.maxThreadCount;
    }

    public void setMaxThreadCount(final int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
        this.setChangesApllied(false);
    }

    @Attribute(readOnly = true)
    public int getState() {
        if (this.connectionApi != null) {
            State state = this.connectionApi.getState(this.connectionGroupName, this.connectionName);
            if (state == null) {
                return -1;
            }
            EState myCurrentState = state.getMyCurrentState();
            return myCurrentState.getEnumIntVal();
        }
        return -1;
    }

    public void setState(int val) {

    }

    @Attribute
    public boolean getConnectionActive() {
        return this.connectionActive;
    }

    public void setConnectionActive(boolean connectionActive) {
        if ((this.connectionApi != null) && (this.smsActivator != null)) {
            if (connectionActive) {
                try {
                    ConnectionDescriptor connectionDescriptor = ConnectionDescriptorMapper.get(this);
                    connectionDescriptor.setCallbackInterface(this.smsActivator.getSmsApiProxy());
                    this.connectionApi.addConnection(connectionDescriptor);
                    this.connectionActive = true;
                    this.internalSetChangesApllied(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            } else {
                try {
                    this.connectionApi.removeConnection(this.connectionGroupName, this.connectionName);
                    this.connectionActive = false;
                    this.internalSetChangesApllied(true);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } else {
            this.connectionActive = false;
        }
    }

    public SmppConnectionApi getConnectionApi() {
        return this.connectionApi;
    }

    public void setConnectionApi(SmppConnectionApi connectionApi) {
        this.connectionApi = connectionApi;
    }

    @Attribute
    public boolean getChangesApllied() {
        return this.changesApllied;
    }

    public void internalSetChangesApllied(boolean changesApllied) {
        this.changesApllied = changesApllied;
    }

    public void setChangesApllied(boolean changesApllied) {
        if (this.connectionActive) {
            if (changesApllied) {
                if (!this.changesApllied) {
                    this.setConnectionActive(true);
                }
            }
        }
        this.changesApllied = changesApllied;
    }

    public SmsActivator getSmsActivator() {
        return this.smsActivator;
    }

    public void setSmsActivator(SmsActivator smsActivator) {
        this.smsActivator = smsActivator;
    }
}
