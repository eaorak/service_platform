package com.adenon.sp.channels;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.channels.channel.ChannelActivator;
import com.adenon.sp.channels.channel.IChannelInfoInternal;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.channels.config.ChannelsConfiguration;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.channels.registry.ChannelCounters;
import com.adenon.sp.kernel.event.SubsystemType;


@MBean(parent = ChannelsConfiguration.class, subLocation = "info=Channels", id = "shortName")
public class ChannelInfo implements IChannelInfoInternal {

    // Not persisted. Should be got in registry when any attribute added as non read-only
    private SubsystemType     subsystem;
    private int               id;
    private String            name;
    private String            shortName;
    private ChannelActivator  activator;
    private IChannelMessaging messaging;
    private IChannelProtocol  protocol;
    private ChannelCounters   counters;

    public ChannelInfo() {
    }

    @Attribute(readOnly = true)
    @Override
    public SubsystemType getSubsystem() {
        return this.subsystem;
    }

    public void setSubsystem(SubsystemType subsystem) {
        this.subsystem = subsystem;
    }

    @Attribute(readOnly = true)
    @Override
    public int getId() {
        return this.id;
    }

    public void setId(int enablerId) {
        this.id = enablerId;
    }

    @Attribute(readOnly = true)
    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String enablerName) {
        this.name = enablerName;
    }

    @Attribute(readOnly = true)
    @Override
    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String enablerShortName) {
        this.shortName = enablerShortName;
    }

    @Override
    public IChannelMessaging getMessaging() {
        return this.messaging;
    }

    public void setMessaging(IChannelMessaging messaging) {
        this.messaging = messaging;
    }

    public ChannelActivator getActivator() {
        return this.activator;
    }

    public void setActivator(ChannelActivator activator) {
        this.activator = activator;
    }

    @Override
    public IChannelProtocol getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(IChannelProtocol protocol) {
        if (protocol == null) {
            throw new RuntimeException("Protocol can not be null !");
        }
        if (this.protocol != null) {
            throw new RuntimeException("Protocol was already set !");
        }
        this.protocol = protocol;
    }

    public ChannelCounters getCounters() {
        return this.counters;
    }

    public void setCounters(ChannelCounters counters) {
        this.counters = counters;
    }

    @Override
    public String toString() {
        return "Enabler [subsystem=" + this.subsystem + ", enablerId=" + this.id + ", enablerName=" + this.name + ", enablerShortName=" + this.shortName + "]";
    }

}
