package com.adenon.channel.sss.api.operation;

import com.adenon.channel.sss.api.message.SmsSendMessage;
import com.adenon.sp.channels.channel.IChannelProvider;
import com.adenon.sp.kernel.dialog.IDialogHandler;


public interface ISmsProvider extends IChannelProvider {

    public void sendSms(SmsSendMessage smsSendMessage, IDialogHandler handler) throws Exception;

}
