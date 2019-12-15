package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;


public class SmsServerDeliverSMMessage extends AbstractMessage {

    private String connectionName;
    private String username;
    private String originating;
    private String destination;
    private String message;
    private int    dataCoding;
    private long   transactionId;


    public SmsServerDeliverSMMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsServerMessageTypes getId() {
        return SmsServerMessageTypes.SERVER_MESSAGE_DELIVERSM;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDataCoding() {
        return this.dataCoding;
    }

    public void setDataCoding(int dataCoding) {
        this.dataCoding = dataCoding;
    }

    public String getOriginating() {
        return this.originating;
    }

    public void setOriginating(String originating) {
        this.originating = originating;
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
