package com.adenon.sp.channel.smsserver.api.message.handler;

import com.adenon.sp.channel.smsserver.api.message.SmsServerBindInfoMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliverSendResultMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDisconnectMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerMessageTypes;
import com.adenon.sp.channel.smsserver.api.message.SmsServerSubmitMessage;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.execution.IRequest;



public abstract class AbstractSmsServerHandler implements IDialogHandler {


    @Override
    public void execute(IRequest request) throws Exception {
        if (request == null) {
            throw new NullPointerException("Request Object is Null");
        }
        IMessage message = request.getMessage();
        if (message == null) {
            throw new NullPointerException("Message Object is Null");
        }
        SmsServerMessageTypes messageTypes = (SmsServerMessageTypes) request.getMessage().getId();
        switch (messageTypes) {
            case SERVER_MESSAGE_BIND:
                this.handleBind(request, (SmsServerBindInfoMessage) message);
                break;
            case SERVER_MESSAGE_SUBMIT:
                this.handleSubmit(request, (SmsServerSubmitMessage) message);
                break;
            case SERVER_MESSAGE_DISCONNECT:
                this.handleDisconnect(request, (SmsServerDisconnectMessage) message);
                break;
            case SERVER_MESSAGE_DELIVER_RESPONSE:
                this.handleDeliverResponse(request, (SmsServerDeliverSendResultMessage) message);
                break;
            default:
                throw new UnsupportedOperationException("Id : " + message.getId() + " has not been supported.");
        }
    }


    @Override
    public void terminate(IRequest request,
                          IError e) {
    }


    public void handleBind(IRequest request,
                           SmsServerBindInfoMessage bindInfoMessage) throws Exception {

    }

    public void handleSubmit(IRequest request,
                             SmsServerSubmitMessage serverSubmitMessage) throws Exception {

    }

    public void handleDisconnect(IRequest request,
                                 SmsServerDisconnectMessage disconnectMessage) throws Exception {

    }

    public void handleDeliverResponse(IRequest request,
                                      SmsServerDeliverSendResultMessage message) throws Exception {

    }

}
