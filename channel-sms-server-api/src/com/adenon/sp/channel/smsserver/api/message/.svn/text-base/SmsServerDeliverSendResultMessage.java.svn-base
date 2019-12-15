package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.sp.channel.smsserver.api.error.SmsServerError;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.IMessageTypes;
import com.adenon.sp.kernel.event.message.MessageType;



public class SmsServerDeliverSendResultMessage extends AbstractMessage {

    private long                transactionId;
    private EDeliverySendResult deliverySendResult;
    private boolean             errorOccured = false;
    private SmsServerError      serverError;
    private IMessage            message;

    public SmsServerDeliverSendResultMessage() {
        super(MessageType.END);
    }


    public long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public EDeliverySendResult getDeliverySendResult() {
        return this.deliverySendResult;
    }

    public void setDeliverySendResult(EDeliverySendResult deliverySendResult) {
        this.deliverySendResult = deliverySendResult;
    }

    @Override
    public Enum<? extends IMessageTypes> getId() {
        return SmsServerMessageTypes.SERVER_MESSAGE_DELIVER_RESPONSE;
    }

    public SmsServerError getServerError() {
        return this.serverError;
    }

    public void setServerError(SmsServerError serverError) {
        this.serverError = serverError;
    }

    public boolean isErrorOccured() {
        return this.errorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }

    public IMessage getMessage() {
        return this.message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }

}
