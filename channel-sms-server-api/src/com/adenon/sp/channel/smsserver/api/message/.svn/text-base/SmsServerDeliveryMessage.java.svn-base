package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.api.smpp.sdk.EDeliveryStatus;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;



public class SmsServerDeliveryMessage extends AbstractMessage {

    private String          connectionName;
    private String          username;
    private String          originating;
    private String          destination;
    private String          messageId;
    private long            transactionId;
    private EDeliveryStatus deliveryStatus;

    public SmsServerDeliveryMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsServerMessageTypes getId() {
        return SmsServerMessageTypes.SERVER_MESSAGE_DELIVERY;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public EDeliveryStatus getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public void setDeliveryStatus(int value) {
        EDeliveryStatus deliveryStatus = EDeliveryStatus.getDeliveryStatus(value);
        this.deliveryStatus = deliveryStatus;
    }

    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getOriginating() {
        return this.originating;
    }

    public void setOriginating(String originating) {
        this.originating = originating;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
