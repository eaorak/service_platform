package com.adenon.sp.channels.configuration;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.kernel.event.message.rule.IDispatchRule;


@MBean(parent = ChannelBean.class, id = "messageName", persist = true)
public class MessageBean {

    private String className;
    private String messageName;
    private String channelName;

    @Attribute
    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Attribute
    public String getMessageName() {
        return this.messageName;
    }

    public void setMessageName(String shortName) {
        this.messageName = shortName;
    }

    @Join
    @Attribute(readOnly = true)
    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void registerToMessage(IDispatchRule dispatchRule) {
        dispatchRule.setMessageName(this.messageName);
    }
}
