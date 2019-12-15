package com.adenon.api.smpp.sdk;

import com.adenon.api.smpp.common.CommonParameters;

public class TextMessageDescriptor implements IMessage {

    private String message;
    private int    dataCoding;

    public String getMessage() {
        return this.message;
    }

    public TextMessageDescriptor setMessage(final String message) {
        this.message = message;
        return this;
    }

    public int getDataCoding() {
        return this.dataCoding;
    }

    public TextMessageDescriptor setDataCoding(final int dataCoding) {
        this.dataCoding = dataCoding;
        return this;
    }

    public static TextMessageDescriptor getAnsiMessageDescriptor(final String message) {
        return new TextMessageDescriptor().setDataCoding(CommonParameters.DATA_CODING_ASCII).setMessage(message);
    }

    public static TextMessageDescriptor getUnicodeMessageDescriptor(final String message) {
        return new TextMessageDescriptor().setDataCoding(CommonParameters.DATA_CODING_UCS2).setMessage(message);
    }

    @Override
    public EMessageType getMessageType() {
        return EMessageType.SMSText;
    }
}
