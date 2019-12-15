package com.adenon.smpp.server.callback.response;


public class SubmitResponse {

    private final ESubmitResult submitResult;
    private final String        messageId;

    public SubmitResponse(ESubmitResult submitResult,
                          String messageId) {
        this.messageId = messageId;
        this.submitResult = submitResult;
    }

    public ESubmitResult getSubmitResult() {
        return this.submitResult;
    }


    public String getMessageId() {
        return this.messageId;
    }


}
