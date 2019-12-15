package com.adenon.api.smpp.sdk;


public interface ISendResult {

    public static final int ERROR_CAUSE_FATAL_ERROR             = 1;
    public static final int ERROR_CAUSE_PROTOCOL_ERROR          = 2;
    public static final int ERROR_CAUSE_INVALID                 = 3;
    public static final int ERROR_CAUSE_INTERNAL_ERROR          = 4;
    public static final int ERROR_CAUSE_NO_CONNECTION_GROUP     = 5;
    public static final int ERROR_CAUSE_NO_CONNECTED_CONNECTION = 6;
    public static final int ERROR_CAUSE_CONNECTION_READONLY     = 7;
    public static final int ERROR_CAUSE_NAME_NOT_VALID          = 8;

    public ConnectionInformation getConnectionInformation();

    public long getTransactionId();

    public ESendResult getSendResult();

    public String getErrorDescription();

    public int getErrorCause();


}
