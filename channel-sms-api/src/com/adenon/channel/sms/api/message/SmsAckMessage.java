package com.adenon.channel.sms.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;


public class SmsAckMessage extends AbstractMessage {

    private String         msgId;
    private long           transactionId;
    private ESendSMSResult sendResult;
    private int            errorCause;
    private String         errorDescription;
    private int            messageCount;

    public SmsAckMessage() {
        super(MessageType.END);
    }

    @Override
    public SmsMessageTypes getId() {
        return SmsMessageTypes.SMS_MESSAGE_SEND_ACK;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }


    public ESendSMSResult getSendResult() {
        return this.sendResult;
    }


    public void setSendResult(ESendSMSResult sendResult) {
        this.sendResult = sendResult;
    }


    public int getErrorCause() {
        return this.errorCause;
    }


    public void setErrorCause(int errorCause) {
        this.errorCause = errorCause;
    }


    public String getErrorDescription() {
        return this.errorDescription;
    }


    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return "SmsAckMessage [msgId="
               + this.msgId
               + ", transactionId="
               + this.transactionId
               + ", sendResult="
               + this.sendResult
               + ", errorCause="
               + this.errorCause
               + ", errorDescription="
               + this.errorDescription
               + "]";
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }


}
