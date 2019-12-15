package com.adenon.sp.channel.webservice;

import com.adenon.sp.channels.channel.ChannelActivator;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.event.SubsystemType;
import com.adenon.sp.kernel.event.message.rpc.RPCMessage;


public class WsActivator extends ChannelActivator {

    @Override
    public void startChannel() {
        //
        for (int i = 0; i < 8; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        final RPCMessage m = new RPCMessage();
                        m.setMethodName("hebele");
                        WsActivator.this.channelMessaging.begin(m, DialogType.STATEFUL, 10000);
                    }
                }
            };// .start();
        }
    }

    @Override
    public void stopChannel() {

    }

    @Override
    public SubsystemType getSubsystem() {
        return SubsystemType.NETWORK;
    }

    @Override
    public String getShortName() {
        return "WEB_SERVICE";
    }

    @Override
    protected IChannelProtocol getProtocol() {
        return new WsChannelProtocol();
    }

}
