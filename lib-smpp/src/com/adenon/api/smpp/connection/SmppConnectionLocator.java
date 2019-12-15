package com.adenon.api.smpp.connection;

import java.util.Iterator;
import java.util.Map;

import com.adenon.api.smpp.common.IndexCounter;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.core.IIOReactor;
import com.adenon.api.smpp.core.SmppIOReactor;

public class SmppConnectionLocator {

    private final IndexCounter               index = new IndexCounter();
    private final SmppConnectionGroupManager connectionGroupManager;
    private final Object                     syncObject;

    public SmppConnectionLocator(final SmppConnectionGroupManager smppConnectionGroupManager,
                                 final Object syncObject) {
        this.connectionGroupManager = smppConnectionGroupManager;
        this.syncObject = syncObject;
    }

    public SmppIOReactor findAvaliableConnection() {
        final SmppConnectionGroup[] clientConnectionsArray = this.connectionGroupManager.getClientConnectionsArray();
        if (clientConnectionsArray != null) {
            int lIndex = this.index.increase(clientConnectionsArray.length);
            for (int j = 0; j < clientConnectionsArray.length; j++) {
                if (lIndex > (clientConnectionsArray.length - 1)) {
                    lIndex = 0;
                }
                if (clientConnectionsArray[lIndex] != null) {
                    final SmppIOReactor ioReactor = clientConnectionsArray[lIndex].getConnectedConnection();
                    if (ioReactor != null) {
                        return ioReactor;
                    }
                }
                lIndex++;
            }
        }
        return null;
    }

    public SmppIOReactor findAvaliableClientConnection(String connectionGroupName,
                                                       String connectionName) throws SmppApiException {
        SmppIOReactor ioReactor = null;
        if ((connectionGroupName == null) || "".equals(connectionGroupName)) {
            ioReactor = this.findAvaliableConnection();
        } else {
            final SmppConnectionGroup connectionGroup = this.connectionGroupManager.get(connectionGroupName);
            if (connectionGroup == null) {
                throw new SmppApiException(SmppApiException.NOT_AVAILABLE, SmppApiException.DOMAIN_SMPP_CONNECTION, "Connection group is not available : "
                                                                                                                    + connectionGroupName);
            }

            if (connectionName == null) {
                ioReactor = connectionGroup.getConnectedConnection();
                if (ioReactor == null) {
                    int counter = 0;
                    while ((ioReactor == null) && (counter < 10)) {
                        try {
                            Thread.sleep(8L);
                        } catch (final Exception e) {
                        }
                        ioReactor = connectionGroup.getConnectedConnection();
                        counter++;
                    }
                }
            } else {
                ioReactor = connectionGroup.getConnectedConnection(connectionName);
                if (ioReactor == null) {
                    int counter = 0;
                    while ((ioReactor == null) && (counter < 10)) {
                        try {
                            Thread.sleep(10L);
                        } catch (final Exception e) {
                        }
                        ioReactor = connectionGroup.getConnectedConnection(connectionName);
                        counter++;
                    }
                }
            }
        }
        return ioReactor;
    }

    public boolean checkConnectionSanity() {
        final IIOReactor ioReactor = this.findAvaliableConnection();
        if (ioReactor == null) {
            return false;
        }
        return true;
    }

    public boolean checkClientConnections(String connectionGroupName) {
        if (this.connectionGroupManager == null) {
            return false;
        }
        final SmppConnectionGroup connectionGroup = this.connectionGroupManager.get(connectionGroupName);
        if (connectionGroup == null) {
            return false;
        }
        return connectionGroup.isThereAnyAliveConnection();
    }

    public int checkHostConnection(String connectionGroupName,
                                   String connectionName) {
        final SmppConnectionGroup customerConnect = this.connectionGroupManager.get(connectionGroupName);
        if (customerConnect == null) {
            return -1;
        }
        final SmppIOReactor customerConnection = customerConnect.getConnection(connectionName);
        if (customerConnection == null) {
            return -1;
        }
        return (customerConnection.checkConnectionSanity() ? 1 : 0);
    }

    public void restartConnectionGroup(String connectionGroupName) {
        final SmppConnectionGroup customerConnect = this.connectionGroupManager.get(connectionGroupName);
        if (customerConnect == null) {
            return;
        }
        customerConnect.restartConnections();
    }

    public void restartHostConnection(String connectionGroupName,
                                      String connectionName) {
        final SmppConnectionGroup customerConnect = this.connectionGroupManager.get(connectionGroupName);
        if (customerConnect == null) {
            return;
        }
        final SmppIOReactor ioReactor = customerConnect.getConnection(connectionName);
        if (ioReactor == null) {
            return;
        }
        ioReactor.restartConnection();
    }

    public void restartAllClientConnection() {
        synchronized (this.syncObject) {
            final Iterator<Map.Entry<String, SmppConnectionGroup>> iterConnection = this.connectionGroupManager.getConnectionGroupControllerIterator();
            while (iterConnection.hasNext()) {
                final Map.Entry<String, SmppConnectionGroup> enumConnection = iterConnection.next();
                final SmppConnectionGroup customerConnect = enumConnection.getValue();
                customerConnect.restartConnections();
            }
        }
    }
}
