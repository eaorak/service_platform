package com.adenon.smpp.server.callback.response;

import com.adenon.api.smpp.common.SmppApiException;


public class BindResponse {

    private final EBindResult bindResult;
    private final String      connectionName;

    public BindResponse(EBindResult bindResult,
                        String connectionName) throws SmppApiException {
        this.bindResult = bindResult;
        this.connectionName = connectionName;
    }

    public EBindResult getBindResult() {
        return this.bindResult;
    }


    public String getConnectionName() {
        return this.connectionName;
    }


}
