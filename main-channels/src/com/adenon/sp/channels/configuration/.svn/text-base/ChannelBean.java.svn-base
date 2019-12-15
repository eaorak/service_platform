package com.adenon.sp.channels.configuration;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.channels.config.ChannelsConfiguration;


@MBean(parent = ChannelsConfiguration.class, subLocation = "channelInformation=Message Routing", id = "shortName", persist = true)
public class ChannelBean {

    private String shortName;

    @Join
    @Attribute
    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
