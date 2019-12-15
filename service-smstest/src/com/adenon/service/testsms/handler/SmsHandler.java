package com.adenon.service.testsms.handler;

import com.adenon.channel.sms.api.message.SmsAckMessage;
import com.adenon.channel.sms.api.message.SmsDeliveryMessage;
import com.adenon.channel.sms.api.message.SmsReceiveMessage;
import com.adenon.channel.sms.api.message.handler.AbstractSmsHandler;
import com.adenon.sp.kernel.execution.IRequest;



public class SmsHandler extends AbstractSmsHandler {


    public SmsHandler() {
    }

    @Override
    public void handleReceive(IRequest request,
                              SmsReceiveMessage receive) {
        System.out.println("receive");

    }

    @Override
    public void handleAcknowledge(IRequest request,
                                  SmsAckMessage ack) {

        System.out.println(ack.toString());

    }

    @Override
    public void handleDelivery(IRequest request,
                               SmsDeliveryMessage delivery) {
        System.out.println("delivery");

    }

}
