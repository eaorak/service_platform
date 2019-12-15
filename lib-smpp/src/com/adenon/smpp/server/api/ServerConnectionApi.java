package com.adenon.smpp.server.api;

import java.util.ArrayList;

import com.adenon.smpp.server.core.ServerApiDelegator;


public class ServerConnectionApi {

    private final ServerApiDelegator serverApiDelegator;

    public ServerConnectionApi(ServerApiDelegator serverApiDelegator) {
        this.serverApiDelegator = serverApiDelegator;
    }

    public boolean closeConnection(String reason,
                                   String serverName,
                                   String connectionName) {
        return this.serverApiDelegator.getConnectionManager().closeConnection(reason, connectionName);
    }

    public ArrayList<String> getConnectedConnectionNames() {
        return this.serverApiDelegator.getConnectionManager().getConnectedConnectionNames();
    }

}
