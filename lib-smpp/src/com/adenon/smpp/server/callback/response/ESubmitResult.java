package com.adenon.smpp.server.callback.response;

import com.adenon.api.smpp.common.Smpp34ErrorCodes;


public enum ESubmitResult {
    submitSuccess(Smpp34ErrorCodes.ERROR_CODE_ROK, "Submit success"),
    QueueFull(Smpp34ErrorCodes.ERROR_CODE_RMSGQFUL, "Queue Full"),
    ThrottleTraffic(Smpp34ErrorCodes.ERROR_CODE_RTHROTTLED, "invalid login"),
    DoNothing(-1, "Dont send ack");

    private final int    value;
    private final String description;

    private ESubmitResult(int value,
                          String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }


    public String getDescription() {
        return this.description;
    }

}
