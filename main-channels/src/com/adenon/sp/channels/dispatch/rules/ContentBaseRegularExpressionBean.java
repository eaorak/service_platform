package com.adenon.sp.channels.dispatch.rules;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;


@MBean(parent = ContentBaseContentBean.class, subLocation = "regularExpression=Regular Expressions", persist = true, id = "regularExpression")
public class ContentBaseRegularExpressionBean implements Comparable<ContentBaseRegularExpressionBean> {

    private String                 regularExpression;
    private String                 uniqueId;
    private int                    index;
    private String                 service;
    private IAdministrationService administrationService;


    @Join
    @Attribute(readOnly = true)
    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Attribute
    public String getRegularExpression() {
        return this.regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    public IAdministrationService getAdministrationService() {
        return this.administrationService;
    }

    public void setAdministrationService(IAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @Attribute
    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(ContentBaseRegularExpressionBean o) {
        return this.index - o.getIndex();
    }

    @Attribute
    public String getService() {
        return this.service;
    }

    public void setService(String serviceName) {
        this.service = this.service;
    }

}
