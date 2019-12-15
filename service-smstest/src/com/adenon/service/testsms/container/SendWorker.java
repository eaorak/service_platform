package com.adenon.service.testsms.container;

import com.adenon.channel.sms.api.message.SmsSendMessage;
import com.adenon.channel.sms.api.operation.ISmsSender;
import com.adenon.service.testsms.handler.SmsHandler;


public class SendWorker implements Runnable {

    private final ISmsSender sendSmsImplementation;
    private final SmsHandler handler;


    public SendWorker(ISmsSender sendSmsImplementation,
                      SmsHandler handler) {
        this.sendSmsImplementation = sendSmsImplementation;
        this.handler = handler;
    }


    @Override
    public void run() {
        SmsSendMessage smsSendMessage = new SmsSendMessage();

        try {
            this.sendSmsImplementation.sendSms(smsSendMessage, this.handler);
        } catch (Exception e) {
        }
    }
}
