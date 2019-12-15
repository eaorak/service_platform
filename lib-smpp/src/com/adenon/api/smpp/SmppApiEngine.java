package com.adenon.api.smpp;

import java.util.Hashtable;
import java.util.Map;

import com.adenon.api.smpp.sdk.ApiProperties;
import com.adenon.api.smpp.sdk.LogDescriptor;

public class SmppApiEngine {

    public static Map<String, SmppApiEngine> instances = new Hashtable<String, SmppApiEngine>();

    private SmppApi                          smppApi;

    private String                           engineName;

    private int                              tpsCount;

    private SmppApiEngine(String engineName) {
        this.engineName = engineName.trim();
        instances.put(this.engineName, this);
    }

    public static synchronized SmppApiEngine getSmppApiEngine(String name,
                                                              int tpsCount) {
        SmppApiEngine apiEngine = instances.get(name);
        if (apiEngine == null) {
            apiEngine = new SmppApiEngine(name);
            apiEngine.tpsCount = tpsCount;
        }
        return apiEngine;
    }

    public SmppApi getSmppApi(LogDescriptor logDescriptor) {
        if (this.smppApi == null) {
            this.smppApi = new SmppApi(this.engineName, logDescriptor, null, this.tpsCount);
        }
        return this.smppApi;
    }

    public SmppApi getSmppApi(LogDescriptor logDescriptor,
                              ApiProperties apiProperties) {
        if (this.smppApi == null) {
            this.smppApi = new SmppApi(this.engineName, logDescriptor, apiProperties, this.tpsCount);
        }
        return this.smppApi;
    }

    public void dispose() {
        instances.remove(this.engineName);
    }

    public int getTpsCount() {
        return this.tpsCount;
    }


}
