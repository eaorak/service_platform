package com.adenon.sp.channels.channel;

import java.util.List;

import com.adenon.sp.kernel.event.SubsystemType;
import com.adenon.sp.kernel.event.message.AbstractMessage;


public class ChannelParams {

    private SubsystemType                subsystem;
    private String                       shortName;
    private ChannelActivator             activator;
    private List<Class<AbstractMessage>> messages;

    public SubsystemType getSubsystem() {
        return this.subsystem;
    }

    public void setSubsystem(SubsystemType subsystem) {
        this.subsystem = subsystem;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ChannelActivator getActivator() {
        return this.activator;
    }

    public void setActivator(ChannelActivator activator) {
        this.activator = activator;
    }

    public void setMessages(List<Class<AbstractMessage>> messages) {
        this.messages = messages;
    }

    public List<Class<AbstractMessage>> getMessages() {
        return this.messages;
    }

}
