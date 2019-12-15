package com.adenon.smpp.server.core;

import java.util.HashMap;


public class ServerConnectionStore {

    private HashMap<String, ServerIOReactor> ioReactorMap = new HashMap<String, ServerIOReactor>();

    public ServerConnectionStore() {
    }

    public void add(String connectionName,
                    ServerIOReactor serverIOReactor) {
        this.ioReactorMap.put(connectionName, serverIOReactor);
    }

    public ServerIOReactor get(String connectionName) {
        return this.ioReactorMap.get(connectionName);
    }

    public ServerIOReactor remove(String connectionName) {
        return this.ioReactorMap.remove(connectionName);
    }

}
