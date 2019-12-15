package com.adenon.sp.channel.test.provider;

import com.adenon.sp.channel.test.TestMessage;
import com.adenon.sp.channel.test.api.ITestChannelProvider;
import com.adenon.sp.channel.test.api.TestReturnMessage;
import com.adenon.sp.channels.api.AbstractProvider;
import com.adenon.sp.channels.api.IApiMessaging;


public class TestChannelProvider extends AbstractProvider implements ITestChannelProvider {

    public TestChannelProvider(IApiMessaging messaging) {
        super(messaging);
    }

    @Override
    public void sendMessage(TestReturnMessage request) throws Exception {
        TestMessage testMessage = new TestMessage();
        // Send to network ...
        this.messaging.sendStatelessMessage(testMessage);
    }

}