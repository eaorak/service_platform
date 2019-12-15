package com.adenon.sp.channel.smsserver.api.message;


public enum ESubmitResponse {
    SUCCESS(0),
    QUEUE_FULL(0x00000014),
    THROTTLE(0x00000058);

    private final int value;

    private ESubmitResponse(int value) {
        this.value = value;

    }

    public int getValue() {
        return this.value;
    }

}
