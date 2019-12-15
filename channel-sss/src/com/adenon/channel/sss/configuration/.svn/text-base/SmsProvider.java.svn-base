package com.adenon.channel.sss.configuration;

import com.adenon.channel.sss.api.message.SmsSendMessage;
import com.adenon.channel.sss.api.operation.ISmsProvider;
import com.adenon.sp.channels.api.AbstractProvider;
import com.adenon.sp.channels.api.IApiMessaging;
import com.adenon.sp.kernel.dialog.IDialogHandler;


public class SmsProvider extends AbstractProvider implements ISmsProvider {

    public SmsProvider(IApiMessaging messaging) {
        super(messaging);
    }

    @Override
    public void sendSms(SmsSendMessage smsSendMessage,
                        IDialogHandler handler) throws Exception {
        this.messaging.sendStatefulMessage(smsSendMessage, handler);
    }

}
