package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.Message;
import com.adenon.sp.kernel.event.message.MessageType;

@Message(messageType = MessageType.BEGIN, dispatchClassNames = {
                                                                "com.adenon.sp.channels.dispatch.rules.DirectRouter",
                                                                "com.adenon.sp.channels.dispatch.rules.ContentBaseRouter",
                                                                "com.adenon.sp.channels.dispatch.rules.DestinationRouter" }, messageName = "SubmitMessage")
public class SmsServerSubmitMessage extends AbstractMessage {

    private String  connectionName;
    private String  userName;
    private String  source;
    private String  destination;
    private String  message;
    private String  messageIdentifier;
    private int     partCount;
    private int     partIndex;
    private int     partReferenceNumber;
    private int     sequenceId;
    private boolean requestDelivery;
    private long    validityPeriod;

    public SmsServerSubmitMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsServerMessageTypes getId() {
        return SmsServerMessageTypes.SERVER_MESSAGE_SUBMIT;
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


    public String getMessage() {
        return this.message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public int getPartCount() {
        return this.partCount;
    }


    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }


    public int getPartIndex() {
        return this.partIndex;
    }


    public void setPartIndex(int partIndex) {
        this.partIndex = partIndex;
    }

    public int getSequenceId() {
        return this.sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getPartReferenceNumber() {
        return this.partReferenceNumber;
    }

    public void setPartReferenceNumber(int partReferenceNumber) {
        this.partReferenceNumber = partReferenceNumber;
    }

    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getMessageIdentifier() {
        return this.messageIdentifier;
    }

    public void setMessageIdentifier(String messageIdentifier) {
        this.messageIdentifier = messageIdentifier;
    }

    public boolean isRequestDelivery() {
        return this.requestDelivery;
    }

    public void setRequestDelivery(boolean requestDelivery) {
        this.requestDelivery = requestDelivery;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getValidityPeriod() {
        return this.validityPeriod;
    }

    public void setValidityPeriod(long validPeriod) {
        this.validityPeriod = validPeriod;
    }

}
