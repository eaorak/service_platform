package com.adenon.sp.service.test;

import com.adenon.channel.sss.api.message.SmsAcknowledge;
import com.adenon.channel.sss.api.message.SmsDelivery;
import com.adenon.channel.sss.api.message.SmsMessage;
import com.adenon.channel.sss.api.message.handler.AbstractSmsHandler;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.execution.IRequest;


public class SmsTestHandler extends AbstractSmsHandler {

    @Override
    public void handleReceive(IRequest request,
                              SmsMessage receive) {
        this.print(request, receive);
    }

    @Override
    public void handleDelivery(IRequest request,
                               SmsDelivery delivery) {
        this.print(request, delivery);
    }

    @Override
    public void handleAcknowledge(IRequest request,
                                  SmsAcknowledge ack) {
        this.print(request, ack);
    }

    private void print(IRequest request,
                       AbstractMessage message) {
        System.out.println(message.getClass().getSimpleName() + " received. Request # Message :: " + request + " # " + message);
    }

}
