package com.adenon.channel.sms.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;

public class SmsReceiveMessage extends AbstractMessage {

    private String source;
    private String destination;
    private String message;
    private int    dataCoding;


    public SmsReceiveMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsMessageTypes getId() {
        return SmsMessageTypes.SMS_MESSAGE_RECEIVED;
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


    public int getDataCoding() {
        return this.dataCoding;
    }


    public void setDataCoding(int dataCoding) {
        this.dataCoding = dataCoding;
    }

}
