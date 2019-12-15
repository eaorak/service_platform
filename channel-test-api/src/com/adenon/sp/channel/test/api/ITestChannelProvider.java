package com.adenon.sp.channel.test.api;

import com.adenon.sp.channels.channel.IChannelProvider;

public interface ITestChannelProvider extends IChannelProvider {

    void sendMessage(TestReturnMessage request) throws Exception;

}
