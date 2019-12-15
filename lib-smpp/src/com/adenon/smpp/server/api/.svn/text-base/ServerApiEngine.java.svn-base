package com.adenon.smpp.server.api;

import java.util.Hashtable;
import java.util.Map;

import com.adenon.api.smpp.sdk.LogDescriptor;
import com.adenon.smpp.server.core.IServerCallback;
import com.adenon.smpp.server.core.ServerApiProperties;

public class ServerApiEngine {

    public static Map<String, ServerApiEngine> instances = new Hashtable<String, ServerApiEngine>();

    private ServerApi                          smppApi;

    private String                             serverName;

    private ServerApiEngine(String serverName) {
        this.serverName = serverName.trim();
        instances.put(this.serverName, this);
    }

    public static synchronized ServerApiEngine getServerApiEngine(String name) {
        ServerApiEngine apiEngine = instances.get(name);
        if (apiEngine == null) {
            apiEngine = new ServerApiEngine(name);
        }
        return apiEngine;
    }

    public ServerApi getServerApi(IServerCallback serverCallback,
                                  LogDescriptor logDescriptor) {
        if (this.smppApi == null) {
            this.smppApi = new ServerApi(this.serverName, serverCallback, logDescriptor, new ServerApiProperties());
        }
        return this.smppApi;
    }

    public ServerApi getServerApi(IServerCallback serverCallback,
                                  LogDescriptor logDescriptor,
                                  ServerApiProperties apiProperties) {
        if (this.smppApi == null) {
            this.smppApi = new ServerApi(this.serverName, serverCallback, logDescriptor, apiProperties);
        }
        return this.smppApi;
    }

    public void dispose() {
        instances.remove(this.serverName);
    }


}
