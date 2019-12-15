package com.adenon.sp.channel.smsserver.api.message;


public enum EDeliverySendResult {
    SUCCESS(0),
    RETRY(1),
    ERROR(2),
    FATAL_ERROR(3);

    private final int value;

    private EDeliverySendResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static final EDeliverySendResult getDeliverySendResult(int val) {
        switch (val) {
            case 0:
                return SUCCESS;
            case 1:
                return RETRY;
            case 2:
                return ERROR;
            case 3:
                return FATAL_ERROR;
            default:
                return FATAL_ERROR;
        }
    }
}
