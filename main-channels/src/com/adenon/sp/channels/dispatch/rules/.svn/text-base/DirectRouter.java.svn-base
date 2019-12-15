package com.adenon.sp.channels.dispatch.rules;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.channels.configuration.MessageBean;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.rule.EDispatchRuleResult;
import com.adenon.sp.kernel.event.message.rule.IDispatchRule;



@MBean(parent = MessageBean.class, subLocation = "directRouter=DirectRouter", persist = true)
public class DirectRouter extends Router implements IDispatchRule {

    private String serviceName;
    private String messageName;

    public DirectRouter() {
    }

    @Override
    public EDispatchRuleResult executeRule(Event event) throws Exception {
        if ((this.getServiceName() == null) || "".equals(this.getServiceName())) {
            return EDispatchRuleResult.CONTINUE;
        }
        event.getDialog().setEndpoint(this.getServiceName());
        return EDispatchRuleResult.END;
    }

    @Attribute
    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Join
    @Attribute(readOnly = true)
    @Override
    public String getMessageName() {
        return this.messageName;
    }

    @Override
    public void setMessageName(String messageName) {
        this.messageName = messageName;

    }

    @Override
    public void registerBeans(Object configuration) {
    }

    @Override
    public void setUniqueId(String uniqueId) {
    }

    @Override
    public String getUniqueId() {
        return "Null";
    }

}
