package com.adenon.channel.sss.api.message.handler;

import java.security.InvalidParameterException;

import com.adenon.channel.sss.api.message.SmsAcknowledge;
import com.adenon.channel.sss.api.message.SmsDelivery;
import com.adenon.channel.sss.api.message.SmsMessage;
import com.adenon.channel.sss.api.message.SmsMessageTypes;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.execution.IRequest;


public abstract class AbstractSmsHandler implements IDialogHandler {

    @Override
    public void execute(IRequest request) throws Exception {
        if (request == null) {
            throw new NullPointerException("Request Object is Null");
        }
        IMessage message = request.getMessage();
        SmsMessageTypes type = (SmsMessageTypes) message.getId();
        if (type == null) {
            throw new InvalidParameterException("Invalid message id [" + message.getId() + "] for SMS !");
        }
        switch (type) {
            case SMS_MESSAGE_RECEIVED:
                this.handleReceive(request, (SmsMessage) message);
                break;
            case SMS_MESSAGE_SEND_ACK:
                this.handleAcknowledge(request, (SmsAcknowledge) message);
                break;
            case SMS_MESSAGE_DELIVER:
                this.handleDelivery(request, (SmsDelivery) message);
                break;
            default:
                throw new UnsupportedOperationException("Id : " + message.getId() + " has not been supported.");
        }
    }

    public void handleReceive(IRequest request,
                              SmsMessage receive) {

    }

    public void handleDelivery(IRequest request,
                               SmsDelivery delivery) {

    }

    public void handleAcknowledge(IRequest request,
                                  SmsAcknowledge ack) {

    }

    public void terminate(IRequest request,
                          IError e) {

    }
}
