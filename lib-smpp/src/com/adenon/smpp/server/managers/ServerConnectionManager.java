package com.adenon.smpp.server.managers;

import java.util.ArrayList;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.core.IIOReactor;
import com.adenon.api.smpp.core.SmppIOReactorStorage;
import com.adenon.smpp.server.core.ServerApiDelegator;
import com.adenon.smpp.server.core.ServerIOReactor;


public class ServerConnectionManager {


    private final ServerApiDelegator serverApiDelegator;

    public ServerConnectionManager(ServerApiDelegator serverApiDelegator) {
        this.serverApiDelegator = serverApiDelegator;
    }

    public boolean closeConnection(String reason,
                                   String connectionName) {
        try {
            SmppIOReactorStorage smppIOReactorStorage = this.serverApiDelegator.getSmppIOReactorStorage();
            ArrayList<IIOReactor> ioReactors = smppIOReactorStorage.getIoReactors();
            for (IIOReactor iioReactor : ioReactors) {
                ServerIOReactor ioReactor = (ServerIOReactor) iioReactor;
                try {
                    if (CommonUtils.checkStringEquality(connectionName, ioReactor.getConnectionInformation().getConnectionName())) {
                        ioReactor.closeConnection(reason);
                        return true;
                    }
                } catch (Exception e) {
                    this.serverApiDelegator.getLogManager()
                                           .getLogger()
                                           .error("ServerConnectionManager", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            this.serverApiDelegator.getLogManager().getLogger().error("ServerConnectionManager", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
        }
        return false;
    }

    public ArrayList<String> getConnectedConnectionNames() {
        try {
            ArrayList<String> connections = new ArrayList<String>();
            SmppIOReactorStorage smppIOReactorStorage = this.serverApiDelegator.getSmppIOReactorStorage();
            ArrayList<IIOReactor> ioReactors = smppIOReactorStorage.getIoReactors();
            for (IIOReactor iioReactor : ioReactors) {
                ServerIOReactor ioReactor = (ServerIOReactor) iioReactor;
                connections.add(ioReactor.getConnectionInformation().getConnectionName());
            }
            return connections;
        } catch (Exception e) {
            this.serverApiDelegator.getLogManager().getLogger().error("ServerConnectionManager", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
        }
        return null;
    }
}
