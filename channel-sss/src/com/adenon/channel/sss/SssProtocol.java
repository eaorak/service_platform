package com.adenon.channel.sss;

import com.adenon.channel.sss.api.message.SmsAcknowledge;
import com.adenon.channel.sss.api.message.SmsDelivery;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.event.Event;


public class SssProtocol implements IChannelProtocol {

    private final IChannelMessaging channelMessaging;

    public SssProtocol(IChannelMessaging channelMessaging) {
        this.channelMessaging = channelMessaging;
    }

    @Override
    public void receiveBegin(Event event) {
        this.msg(event);
        this.channelMessaging.continuee(event.getDialog(), new SmsAcknowledge());
        this.channelMessaging.end(event.getDialog(), new SmsDelivery());
    }

    @Override
    public void receiveContinue(Event event) {
        throw new RuntimeException("SMS channel can not receive continue events ! [" + event + "]");
    }

    @Override
    public void receiveEnd(Event event) {
        throw new RuntimeException("SMS channel can not receive end events ! [" + event + "]");
    }

    @Override
    public void receiveFail(Event event) {
        this.msg(event);
    }

    private void msg(Event event) {
        System.out.println(event);
    }

}
