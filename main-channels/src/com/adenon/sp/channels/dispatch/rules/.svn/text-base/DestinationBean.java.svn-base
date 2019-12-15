package com.adenon.sp.channels.dispatch.rules;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;


@MBean(parent = DestinationRouter.class, persist = true, id = "destination")
public class DestinationBean {

    private String destination;
    private String serviceName;
    private String uniqueId;

    @Attribute
    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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
    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

}
