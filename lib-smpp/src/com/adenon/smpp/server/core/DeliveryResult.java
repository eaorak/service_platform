package com.adenon.smpp.server.core;

import com.adenon.api.smpp.message.DeliverSMMessage;


public class DeliveryResult implements IDeliveryResult {

    private EDeliveryResult  deliveryResult;
    private String           errorDescription;
    private int              errorCause;
    private DeliverSMMessage message;
    private long             transactionId;

    public DeliveryResult(EDeliveryResult deliveryResult,
                          int errorCause,
                          String errorDescription,
                          DeliverSMMessage message,
                          long transactionId) {
        this.deliveryResult = deliveryResult;
        this.errorCause = errorCause;
        this.errorDescription = errorDescription;
        this.message = message;
        this.transactionId = transactionId;
    }


    @Override
    public EDeliveryResult getDeliveryResult() {
        return this.deliveryResult;
    }

    @Override
    public String getErrorDescription() {
        return this.errorDescription;
    }

    @Override
    public int getErrorCause() {
        return this.errorCause;
    }

    @Override
    public DeliverSMMessage getMessage() {
        return this.message;
    }


    @Override
    public long getTransactionId() {
        return this.transactionId;
    }

    public void setDeliveryResult(EDeliveryResult deliveryResult) {
        this.deliveryResult = deliveryResult;
    }


    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }


    public void setErrorCause(int errorCause) {
        this.errorCause = errorCause;
    }


    public void setMessage(DeliverSMMessage message) {
        this.message = message;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }


}
