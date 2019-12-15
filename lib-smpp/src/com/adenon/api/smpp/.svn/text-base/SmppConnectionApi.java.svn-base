package com.adenon.api.smpp;

import com.adenon.api.smpp.common.State;
import com.adenon.api.smpp.connection.SmppConnectionGroup;
import com.adenon.api.smpp.core.SmppApiDelegator;
import com.adenon.api.smpp.core.SmppIOReactor;
import com.adenon.api.smpp.sdk.ConnectionDescriptor;
import com.adenon.api.smpp.sdk.ConnectionGroupDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInformation;

public class SmppConnectionApi {

    private SmppApiDelegator smppApiDelegator;

    public SmppConnectionApi(SmppApiDelegator smppApiDelegator) {
        this.smppApiDelegator = smppApiDelegator;
    }

    public void addConnection(ConnectionDescriptor connectionDescriptor) throws Exception {
        connectionDescriptor.validate();
        this.smppApiDelegator.getConnectionGroupManager().addConnection(connectionDescriptor.getConnectionGroupName(), connectionDescriptor);
    }

    public void createConnectionGroup(ConnectionGroupDescriptor connectionGroupDescriptor) throws Exception {
        this.smppApiDelegator.getConnectionGroupManager().createConnectionGroup(connectionGroupDescriptor);
    }

    public ConnectionGroupDescriptor generateConnectionGroup(String connectionGroupName) {
        ConnectionGroupDescriptor connectionGroupDescriptor = new ConnectionGroupDescriptor(connectionGroupName);
        return connectionGroupDescriptor;
    }

    public State getConnectionGroupState(String connectionGroupName) {
        SmppConnectionGroup smppConnectionGroup = this.smppApiDelegator.getConnectionGroupManager().get(connectionGroupName);
        if (smppConnectionGroup != null) {
            return smppConnectionGroup.getStateHigherAuthority();
        }
        return null;
    }

    public State getState() {
        return this.smppApiDelegator.getConnectionGroupManager().getStateHigherAuthority();
    }

    public State getState(String connectionGroupName,
                          String connectionName) {
        try {
            SmppConnectionGroup smppConnectionGroup = this.smppApiDelegator.getConnectionGroupManager().get(connectionGroupName);
            SmppIOReactor connection = smppConnectionGroup.getConnection(connectionName);
            ConnectionInformation connectionInformation = connection.getConnectionInformation();
            return connectionInformation.getConnectionState();
        } catch (Exception e) {
        }
        return null;
    }

    public void removeConnection(String connectionGroupName,
                                 String connectionName) {
        this.smppApiDelegator.getConnectionGroupManager().removeConnection(connectionGroupName, connectionName);
    }

    public void removeConnectionGroup(String connectionGroupName) {
        this.smppApiDelegator.getConnectionGroupManager().removeClient(connectionGroupName);

    }

    public void shutdownAllConnectionGroup() {
        this.smppApiDelegator.getConnectionGroupManager().shutdown();
    }

    public void shutdownConnectionGroup(String connectionGroupName) {
        this.smppApiDelegator.getConnectionGroupManager().shutdown(connectionGroupName);
    }


}
