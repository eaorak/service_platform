package com.adenon.sp.channels.dispatch.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adenon.library.common.utils.StringUtils;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;



@MBean(parent = ContentBaseRouter.class, persist = true, id = "destination")
public class ContentBaseDestinationBean {

    private final Map<String, ContentBaseContentBean>    serviceMap                 = new HashMap<String, ContentBaseContentBean>();

    private String                                       destination;
    private String                                       uniqueId;
    private IAdministrationService                       administrationService;
    private final Map<String, ContentBaseExactMatchBean> exactMatchMap              = new HashMap<String, ContentBaseExactMatchBean>();
    private final List<ContentBaseRegularExpressionBean> baseRegularExpressionBeans = new ArrayList<ContentBaseRegularExpressionBean>();


    @Attribute
    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    // @BeanId
    @Join
    @Attribute(readOnly = true)
    public String getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Operation
    public String addService(@Parameter(name = "service") final String service) throws Exception {
        if (StringUtils.checkStringIsEmpty(service)) {
            return "service can not be empty";
        }
        if (this.serviceMap.containsKey(service)) {
            return "ERROR : Service : " + service + " already has been created. Please remove it first. ";
        }
        ContentBaseContentBean contentBean = new ContentBaseContentBean();
        contentBean.setService(service);
        contentBean.setUniqueId(this.uniqueId);
        contentBean.setContentBaseDestinationBean(this);
        contentBean.setAdministrationService(this.administrationService);
        this.serviceMap.put(service, contentBean);
        this.getAdministrationService().registerBean(contentBean);
        return "Service entry : " + service + " created successfully.";
    }

    @Operation
    public String removeService(@Parameter(name = "service") final String service) throws Exception {
        if (StringUtils.checkStringIsEmpty(service)) {
            return "service can not be empty";
        }
        ContentBaseContentBean contentBean = this.serviceMap.remove(service);
        if (contentBean != null) {
            List<String> exactMatchList = contentBean.getExactMatchList();
            for (String contentString : exactMatchList) {
                contentBean.removeExactMatch(contentString);
            }
            this.getAdministrationService().unregisterBean(contentBean);
        }

        return "Service entry : " + service + " created successfully.";
    }

    public void registerBeans() throws Exception {
        IBeanHelper<ContentBaseContentBean> destinationBeansHelper = this.administrationService.getBeans(ContentBaseContentBean.class);
        IBeanHelper<ContentBaseContentBean> childsOf = destinationBeansHelper.getChildsOf(this);
        List<ContentBaseContentBean> allBeans = childsOf.getAllBeans();
        childsOf.register();
        for (ContentBaseContentBean contentBean : allBeans) {
            contentBean.setAdministrationService(this.administrationService);
            contentBean.setContentBaseDestinationBean(this);
            contentBean.registerBeans();
            this.serviceMap.put(contentBean.getService(), contentBean);
        }

    }

    public IAdministrationService getAdministrationService() {
        return this.administrationService;
    }

    public void setAdministrationService(IAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    public Map<String, ContentBaseExactMatchBean> getExactMatchMap() {
        return this.exactMatchMap;
    }

    public List<ContentBaseRegularExpressionBean> getBaseRegularExpressionBeans() {
        return this.baseRegularExpressionBeans;
    }


}
