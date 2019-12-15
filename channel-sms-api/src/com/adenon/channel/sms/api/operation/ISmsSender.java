package com.adenon.channel.sms.api.operation;

import com.adenon.channel.sms.api.message.SmsSendMessage;
import com.adenon.sp.kernel.dialog.IDialogHandler;



public interface ISmsSender {

    public void sendSms(SmsSendMessage smsSendMessage,
                        IDialogHandler handler) throws Exception;

}
