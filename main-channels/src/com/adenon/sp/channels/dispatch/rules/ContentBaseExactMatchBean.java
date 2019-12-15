package com.adenon.sp.channels.dispatch.rules;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;


@MBean(parent = ContentBaseContentBean.class, subLocation = "exactMatch=Exact Match", persist = true, id = "content")
public class ContentBaseExactMatchBean {

    private String                 content;
    private String                 uniqueId;
    private String                 service;
    private IAdministrationService administrationService;


    @Attribute(readOnly = true)
    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Attribute
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public IAdministrationService getAdministrationService() {
        return this.administrationService;
    }

    public void setAdministrationService(IAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @Join
    @Attribute
    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
