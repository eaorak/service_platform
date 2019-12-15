package com.adenon.sp.channels.dispatch.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.adenon.library.common.utils.StringUtils;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;



@MBean(parent = ContentBaseDestinationBean.class, persist = true, id = "service")
public class ContentBaseContentBean {


    private String                     service;
    private String                     uniqueId;
    private IAdministrationService     administrationService;
    private ContentBaseDestinationBean contentBaseDestinationBean;
    private final List<String>         exactMatchList = new ArrayList<String>();


    // @BeanId
    @Join
    @Attribute(readOnly = true)
    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Attribute
    public String getService() {
        return this.service;
    }

    public void setService(String serviceName) {
        this.service = serviceName;
    }

    public IAdministrationService getAdministrationService() {
        return this.administrationService;
    }

    public void setAdministrationService(IAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    public void registerBeans() throws Exception {
        IBeanHelper<ContentBaseExactMatchBean> destinationBeansHelper = this.administrationService.getBeans(ContentBaseExactMatchBean.class);
        IBeanHelper<ContentBaseExactMatchBean> childsOf = destinationBeansHelper.getChildsOf(this);
        List<ContentBaseExactMatchBean> allBeans = childsOf.getAllBeans();
        childsOf.register();
        for (ContentBaseExactMatchBean destinationBean : allBeans) {
            destinationBean.setAdministrationService(this.administrationService);
            this.contentBaseDestinationBean.getExactMatchMap().put(destinationBean.getContent(), destinationBean);
            this.getExactMatchList().add(destinationBean.getContent());
        }
        IBeanHelper<ContentBaseRegularExpressionBean> regularExpressionHelper = this.administrationService.getBeans(ContentBaseRegularExpressionBean.class);
        IBeanHelper<ContentBaseRegularExpressionBean> childsOfContentForRegularExpression = regularExpressionHelper.getChildsOf(this);
        List<ContentBaseRegularExpressionBean> allBeansRegularExpression = childsOfContentForRegularExpression.getAllBeans();
        childsOfContentForRegularExpression.register();
        for (ContentBaseRegularExpressionBean destinationBean : allBeansRegularExpression) {
            destinationBean.setAdministrationService(this.administrationService);
            this.contentBaseDestinationBean.getBaseRegularExpressionBeans().add(destinationBean);
        }


    }

    @Operation
    public String addRegularExpression(@Parameter(name = "regular expression") final String regularExpression,
                                       @Parameter(name = "index") int index) throws Exception {
        if (StringUtils.checkStringIsEmpty(regularExpression)) {
            return "regular expression can not be empty";
        }
        ContentBaseRegularExpressionBean contentBean = new ContentBaseRegularExpressionBean();
        contentBean.setRegularExpression(regularExpression);
        contentBean.setIndex(index);
        contentBean.setUniqueId(this.uniqueId);
        contentBean.setService(this.service);
        this.contentBaseDestinationBean.getBaseRegularExpressionBeans().add(contentBean);
        Collections.sort(this.contentBaseDestinationBean.getBaseRegularExpressionBeans());
        this.getAdministrationService().registerBean(contentBean);
        return "Regular Expression entry : " + regularExpression + " created successfully.";
    }

    @Operation
    public String addExactMatch(@Parameter(name = "content") final String content) throws Exception {
        if (StringUtils.checkStringIsEmpty(content)) {
            return "content can not be empty";
        }
        ContentBaseExactMatchBean contentBean = new ContentBaseExactMatchBean();
        contentBean.setContent(content);
        contentBean.setUniqueId(this.uniqueId);
        contentBean.setService(this.service);
        this.contentBaseDestinationBean.getExactMatchMap().put(content, contentBean);
        this.getAdministrationService().registerBean(contentBean);
        this.getExactMatchList().add(content);
        return "Exact match entry : " + content + " created successfully.";
    }


    public ContentBaseDestinationBean getContentBaseDestinationBean() {
        return this.contentBaseDestinationBean;
    }

    public void setContentBaseDestinationBean(ContentBaseDestinationBean contentBaseDestinationBean) {
        this.contentBaseDestinationBean = contentBaseDestinationBean;
    }

    @Operation
    public String removeExactMatch(@Parameter(name = "content") final String content) throws Exception {
        if (StringUtils.checkStringIsEmpty(content)) {
            return "content can not be empty";
        }
        ContentBaseExactMatchBean removedBean = this.contentBaseDestinationBean.getExactMatchMap().remove(content);
        if (removedBean == null) {
            return "Wrong exactMatch entry : " + content;
        }
        this.getAdministrationService().unregisterBean(removedBean);
        this.getExactMatchList().remove(content);
        return "Sucessfully removed from list : " + content;
    }

    public List<String> getExactMatchList() {
        return this.exactMatchList;
    }


}
