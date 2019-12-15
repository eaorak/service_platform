package com.adenon.api.smpp.connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.adenon.api.smpp.common.IndexCounter;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.common.State;
import com.adenon.api.smpp.common.StateHigherAuthority;
import com.adenon.api.smpp.core.IIOReactor;
import com.adenon.api.smpp.core.SmppIOReactor;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.sdk.SmppConnectionType;

public class SmppConnectionGroup {

    private final LoggerWrapper        logger;

    private final IndexCounter         index                = new IndexCounter();

    private Map<String, SmppIOReactor> ioStorageMap         = null;
    private String                     connectionGroupName;
    private final State                operationState       = new State();
    private SmppIOReactor[]            connections;
    private Object                     lockObject           = new Object();
    private StateHigherAuthority       stateHigherAuthority = new StateHigherAuthority();


    public SmppConnectionGroup(String connectionGroupName,
                               LoggerWrapper logger,
                               StateHigherAuthority higherAuthorityParent) {
        higherAuthorityParent.addState(this.stateHigherAuthority);
        this.logger = logger;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("SmppConnectionGroup", "SmppConnectionGroup", 0, null, "Connection group created " + connectionGroupName);
        }

        this.setConnectionGroupName(connectionGroupName);
        this.ioStorageMap = new HashMap<String, SmppIOReactor>();
        this.operationState.idle();
    }

    public Iterator<Map.Entry<String, SmppIOReactor>> entries() {
        return this.ioStorageMap.entrySet().iterator();
    }

    public void addConnection(SmppIOReactor ioReactor) {
        synchronized (this.getLockObject()) {
            try {
                this.operationState.stopped();
                this.setConnections(null);
                this.ioStorageMap.put(ioReactor.getConnectionDescriptor().getConnectionName(), ioReactor);
                this.fillArray();
            } catch (final Exception e) {
                this.logger.error("SmppConnectionGroup", "addConnection", 0, null, " : Error : " + e.getMessage(), e);
            } finally {
                this.operationState.idle();
            }
        }
    }

    public void removeConnection(String connectionName) {
        synchronized (this.getLockObject()) {
            if (connectionName == null) {
                return;
            }
            IIOReactor ioReactor = null;
            try {
                this.operationState.stopped();
                ioReactor = this.ioStorageMap.get(connectionName);
                if (ioReactor != null) {
                    this.ioStorageMap.remove(connectionName);
                    this.fillArray();
                }
            } catch (final Exception e) {
                this.logger.error("SmppConnectionGroup", "removeConnection", 0, null, " : Error : " + e.getMessage(), e);
            } finally {
                try {
                    if (ioReactor != null) {
                        ioReactor.shutdown();
                    }
                } catch (final Exception e2) {
                }
                this.operationState.idle();
            }
        }
    }

    private void fillArray() {
        synchronized (this.getLockObject()) {
            try {
                if (this.ioStorageMap.size() > 0) {
                    this.setConnections(new SmppIOReactor[this.ioStorageMap.size()]);
                    Map.Entry<String, SmppIOReactor> cc = null;
                    final Iterator<Map.Entry<String, SmppIOReactor>> iterConnection = this.ioStorageMap.entrySet().iterator();
                    int currentIndex = 0;
                    while (iterConnection.hasNext()) {
                        cc = iterConnection.next();
                        this.getConnections()[currentIndex] = cc.getValue();
                        currentIndex++;
                    }
                } else {
                    this.setConnections(null);
                }
            } catch (final Exception e) {
                this.logger.error("SmppConnectionGroup", "fillArray", 0, null, " : Error : " + e.getMessage(), e);
            }
        }
    }

    public SmppIOReactor getConnectedConnection() {
        if (this.getConnections() == null) {
            return null;
        }
        int connectionIndex = this.index.increase(this.getConnections().length);
        this.operationState.waitIdle();
        for (int j = 0; j < this.getConnections().length; j++) {
            if (connectionIndex > (this.getConnections().length - 1)) {
                connectionIndex = 0;
            }
            if (this.getConnections()[connectionIndex] != null) {
                if (this.getConnections()[connectionIndex].checkConnectionSanity()) {
                    if (this.getConnections()[connectionIndex].getUsedBufferCount() < (this.getConnections()[connectionIndex].getWindowSize() - 2)) {
                        if (this.getConnections()[connectionIndex].getConnectionType() != SmppConnectionType.READ) {
                            return this.getConnections()[connectionIndex];
                        }
                    }
                }
            }
            connectionIndex++;
        }
        for (int j = 0; j < this.getConnections().length; j++) {
            connectionIndex++;
            if (connectionIndex > (this.getConnections().length - 1)) {
                connectionIndex = 0;
            }
            if (this.getConnections()[connectionIndex] != null) {
                if (this.getConnections()[connectionIndex].checkConnectionSanity()) {
                    if (this.getConnections()[connectionIndex].getConnectionType() != SmppConnectionType.READ) {
                        return this.getConnections()[connectionIndex];
                    }
                }
            }
        }
        return null;
    }

    public SmppIOReactor getConnectedConnection(final String connectionName) throws SmppApiException {
        final SmppIOReactor customerConnection = this.ioStorageMap.get(connectionName);
        if (customerConnection == null) {
            throw new SmppApiException(SmppApiException.NOT_AVAILABLE, SmppApiException.DOMAIN_SMPP_CONNECTION, "host : "
                                                                                                                + connectionName
                                                                                                                + " not on the client list.");
        }
        if (customerConnection.checkConnectionSanity()) {
            return customerConnection;
        }
        return null;
    }

    public SmppIOReactor getConnection(final String connectionName) {
        return this.ioStorageMap.get(connectionName);
    }

    public boolean isThereAnyAliveConnection() {
        synchronized (this.getLockObject()) {
            SmppIOReactor ioReactor = null;
            Map.Entry<String, SmppIOReactor> mapCc = null;
            final Iterator<Map.Entry<String, SmppIOReactor>> iterConnection = this.ioStorageMap.entrySet().iterator();
            while (iterConnection.hasNext()) {
                mapCc = iterConnection.next();
                ioReactor = mapCc.getValue();
                if (ioReactor.checkConnectionSanity()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getSize() {
        return this.ioStorageMap.size();
    }

    public void restartConnections() {
        synchronized (this.getLockObject()) {
            SmppIOReactor ioReactor = null;
            Map.Entry<String, SmppIOReactor> mapCc = null;
            final Iterator<Map.Entry<String, SmppIOReactor>> iterConnection = this.ioStorageMap.entrySet().iterator();
            while (iterConnection.hasNext()) {
                mapCc = iterConnection.next();
                ioReactor = mapCc.getValue();
                ioReactor.restartConnection();
            }
        }
    }

    public void shutdown() {
        synchronized (this.getLockObject()) {
            IIOReactor ioReactor = null;
            Map.Entry<String, SmppIOReactor> mapCc = null;
            final Iterator<Map.Entry<String, SmppIOReactor>> iterConnection = this.ioStorageMap.entrySet().iterator();
            while (iterConnection.hasNext()) {
                mapCc = iterConnection.next();
                ioReactor = mapCc.getValue();
                try {
                    ioReactor.shutdown();
                } catch (final Exception e) {
                    this.logger.error("SmppConnectionGroup", "shutdown", 0, null, " : Error : " + e.getMessage(), e);
                }
            }
            this.ioStorageMap.clear();
            this.ioStorageMap = null;
            this.setConnections(null);
        }
    }

    @Override
    public String toString() {
        return this.getConnectionGroupName();
    }

    public String getConnectionGroupName() {
        return this.connectionGroupName;
    }

    public void setConnectionGroupName(final String client) {
        this.connectionGroupName = client;
    }

    public SmppIOReactor[] getConnections() {
        return this.connections;
    }

    public void setConnections(final SmppIOReactor[] connections) {
        this.connections = connections;
    }

    public Object getLockObject() {
        return this.lockObject;
    }

    public void setLockObject(final Object lockObject) {
        this.lockObject = lockObject;
    }

    public StateHigherAuthority getStateHigherAuthority() {
        return this.stateHigherAuthority;
    }

    public void setStateHigherAuthority(final StateHigherAuthority stateHigherAuthority) {
        this.stateHigherAuthority = stateHigherAuthority;
    }

}
