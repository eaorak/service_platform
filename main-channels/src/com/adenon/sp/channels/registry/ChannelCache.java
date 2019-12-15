package com.adenon.sp.channels.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adenon.sp.channels.ChannelInfo;
import com.adenon.sp.channels.channel.IChannelInfo;


public class ChannelCache {

    private Map<String, ChannelInfo> nameMap = new ConcurrentHashMap<String, ChannelInfo>();

    public void add(ChannelInfo channel) {
        this.nameMap.put(channel.getName(), channel);
    }

    public void check(String name,
                      int id,
                      String shortName) throws Exception {
        if ((this.getByName(name) != null) || (this.getById(id) != null) || (this.getByShortName(shortName) != null)) {
            throw new RuntimeException("Channel [" + id + "|" + shortName + "|" + name + "] already exist !");
        }
    }

    public ChannelInfo remove(IChannelInfo channel) {
        return this.nameMap.remove(channel.getName());
    }

    public ChannelInfo getByName(String channelName) {
        return this.nameMap.get(channelName);
    }

    public ChannelInfo getById(Integer channelId) {
        for (ChannelInfo enabler : this.nameMap.values()) {
            if (enabler.getId() == channelId) {
                return enabler;
            }
        }
        return null;
    }

    public ChannelInfo getByShortName(String shortName) {
        for (ChannelInfo channel : this.nameMap.values()) {
            if (channel.getShortName().equals(shortName)) {
                return channel;
            }
        }
        return null;
    }
}