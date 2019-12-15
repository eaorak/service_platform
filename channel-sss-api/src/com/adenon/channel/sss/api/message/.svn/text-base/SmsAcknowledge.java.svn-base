package com.adenon.channel.sss.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;

public class SmsAcknowledge extends AbstractMessage {

    private String msgId;
    private int    transactionId;

    public SmsAcknowledge() {
        super(MessageType.CONTINUE);
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

    public int getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

}
