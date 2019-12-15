package com.adenon.sp.channel.test;

import com.adenon.sp.channel.test.api.ITestChannelProvider;
import com.adenon.sp.channel.test.provider.TestChannelProvider;
import com.adenon.sp.channels.channel.ChannelActivator;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.event.SubsystemType;


public class TestChannelActivator extends ChannelActivator {

    @Override
    public void startChannel() {
        this.registerService(ITestChannelProvider.class, new TestChannelProvider(this.apiMessaging));
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
        return "TEST";
    }

    @Override
    protected IChannelProtocol getProtocol() {
        return new MessageProcessor();
    }

}
