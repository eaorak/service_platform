package com.adenon.channel.sms.api.message.handler;

import com.adenon.channel.sms.api.message.SmsAckMessage;
import com.adenon.channel.sms.api.message.SmsDeliveryMessage;
import com.adenon.channel.sms.api.message.SmsMessageTypes;
import com.adenon.channel.sms.api.message.SmsReceiveMessage;
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
        if (message == null) {
            throw new NullPointerException("Message Object is Null");
        }
        SmsMessageTypes messageTypes = (SmsMessageTypes) request.getMessage().getId();
        switch (messageTypes) {
            case SMS_MESSAGE_RECEIVED:
                this.handleReceive(request, (SmsReceiveMessage) message);
                break;
            case SMS_MESSAGE_DELIVER:
                this.handleDelivery(request, (SmsDeliveryMessage) message);
                break;
            case SMS_MESSAGE_SEND_ACK:
                this.handleAcknowledge(request, (SmsAckMessage) message);
                break;
            default:
                throw new UnsupportedOperationException("Id : " + message.getId() + " has not been supported.");
        }
    }

    @Override
    public void terminate(IRequest request,
                          IError e) {
    }


    public void handleReceive(IRequest request,
                              SmsReceiveMessage receive) {

    }

    public void handleAcknowledge(IRequest request,
                                  SmsAckMessage ack) {

    }

    public void handleDelivery(IRequest request,
                               SmsDeliveryMessage delivery) {

    }


}
