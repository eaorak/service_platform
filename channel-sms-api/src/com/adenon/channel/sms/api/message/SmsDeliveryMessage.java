package com.adenon.channel.sms.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;


public class SmsDeliveryMessage extends AbstractMessage {

    private String source;
    private String destination;
    private String messageId;


    public SmsDeliveryMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsMessageTypes getId() {
        return SmsMessageTypes.SMS_MESSAGE_DELIVER;
    }


    public String getSource() {
        return this.source;
    }


    public void setSource(String source) {
        this.source = source;
    }


    public String getDestination() {
        return this.destination;
    }


    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getMessageId() {
        return this.messageId;
    }


    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
