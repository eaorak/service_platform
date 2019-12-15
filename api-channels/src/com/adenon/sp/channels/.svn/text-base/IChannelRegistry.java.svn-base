package com.adenon.sp.channels;

import com.adenon.sp.channels.channel.ChannelParams;
import com.adenon.sp.channels.channel.IChannelInfo;
import com.adenon.sp.channels.channel.IChannelInfoInternal;
import com.adenon.sp.kernel.osgi.BundleInfo;


public interface IChannelRegistry {

    IChannelInfo findById(int id);

    IChannelInfo findByShortName(String shortName);

    IChannelInfoInternal register(BundleInfo bundle,
                                  ChannelParams params) throws Exception;

    void unregister(IChannelInfo channel) throws Exception;

}
