package com.adenon.api.smpp.core;

import com.adenon.api.smpp.messaging.processor.IMessageHandler;


public class SingleMessageHandler implements IMessageHandler {

    private int    sequenceNumber;
    private String messageIdentifier;

    @Override
    public boolean responseReceived(int sequenceNumber,
                                    String messageIdentifier) {
        if (this.sequenceNumber == sequenceNumber) {
            return true;
        }
        return false;
    }

    @Override
    public void addSequence(int msgIndex,
                            int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;

    }

    @Override
    public void errorReceived() {

    }

    @Override
    public boolean isLastSegment(int sequenceNumber) {
        return true;
    }

    @Override
    public String getMessageIdentifier() {
        return this.messageIdentifier;
    }

    @Override
    public int getMessagePartCount() {
        return 1;
    }

}
