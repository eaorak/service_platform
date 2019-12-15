package com.adenon.channel.sms.provider;

import com.adenon.channel.sms.api.message.SmsSendMessage;
import com.adenon.channel.sms.api.operation.ISmsSender;
import com.adenon.sp.channels.api.AbstractProvider;
import com.adenon.sp.channels.api.IApiMessaging;
import com.adenon.sp.kernel.dialog.IDialogHandler;


public class SmsMessageProvider extends AbstractProvider implements ISmsSender {

    public SmsMessageProvider(IApiMessaging messaging) {
        super(messaging);
    }

    @Override
    public void sendSms(SmsSendMessage smsSendMessage, IDialogHandler handler) throws Exception {
        this.messaging.sendStatefulMessage(smsSendMessage, handler);
    }

}
