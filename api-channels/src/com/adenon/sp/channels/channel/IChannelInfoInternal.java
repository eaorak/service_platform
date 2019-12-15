package com.adenon.sp.channels.channel;

import com.adenon.sp.channels.channel.messaging.IChannelProtocol;

public interface IChannelInfoInternal extends IChannelInfo {

    void setProtocol(IChannelProtocol protocol);
}
