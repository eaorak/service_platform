package com.adenon.sp.channels.dispatch.rules;

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
import com.adenon.sp.channels.configuration.MessageBean;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.AbstractDefaultMessage;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.rule.EDispatchRuleResult;
import com.adenon.sp.kernel.event.message.rule.IDispatchRule;


@MBean(parent = MessageBean.class, subLocation = "contentRouter=ContentBaseRouter", persist = true)
public class ContentBaseRouter extends Router implements IDispatchRule {

    private String                                        messageName;
    private final Map<String, ContentBaseDestinationBean> destinationMap = new HashMap<String, ContentBaseDestinationBean>();

    private IAdministrationService                        administrationService;
    private String                                        uniqueId;

    @Override
    public EDispatchRuleResult executeRule(Event event) throws Exception {
        IMessage message = event.getMessage();
        if (message instanceof AbstractDefaultMessage) {
            AbstractDefaultMessage abstractDefaultMessage = (AbstractDefaultMessage) message;
            String destination = abstractDefaultMessage.getDestination();
            ContentBaseDestinationBean destinationBean = this.destinationMap.get(destination);
            if (destinationBean != null) {
                ContentBaseExactMatchBean contentBaseExactMatchBean = destinationBean.getExactMatchMap().get(abstractDefaultMessage.getMessageArgument());
                if (contentBaseExactMatchBean != null) {
                    String serviceClass = contentBaseExactMatchBean.getService();
                    event.getDialog().setEndpoint(serviceClass);
                    return EDispatchRuleResult.END;
                }
                List<ContentBaseRegularExpressionBean> baseRegularExpressionBeans = destinationBean.getBaseRegularExpressionBeans();
                if (baseRegularExpressionBeans.size() > 0) {
                    String messageArgument = abstractDefaultMessage.getMessageArgument();
                    for (ContentBaseRegularExpressionBean contentBaseRegularExpressionBean : baseRegularExpressionBeans) {
                        String regularExpression = contentBaseRegularExpressionBean.getRegularExpression();
                        if (messageArgument.matches(regularExpression)) {
                            String serviceClass = contentBaseRegularExpressionBean.getService();
                            event.getDialog().setEndpoint(serviceClass);
                            return EDispatchRuleResult.END;
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Message : " + message + " should extend AbstractDefaultMessage");
        }
        return EDispatchRuleResult.CONTINUE;
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
    public void registerBeans(Object configuration) throws Exception {
        this.administrationService = (IAdministrationService) configuration;
        IBeanHelper<ContentBaseDestinationBean> destinationBeansHelper = this.administrationService.getBeans(ContentBaseDestinationBean.class);
        IBeanHelper<ContentBaseDestinationBean> childsOf = destinationBeansHelper.getChildsOf(this);
        List<ContentBaseDestinationBean> allBeans = childsOf.getAllBeans();
        childsOf.register();
        for (ContentBaseDestinationBean destinationBean : allBeans) {
            destinationBean.setAdministrationService(this.administrationService);
            destinationBean.registerBeans();
            this.destinationMap.put(destinationBean.getDestination(), destinationBean);
        }

    }


    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Operation
    public String addDestination(@Parameter(name = "destination") final String destination) throws Exception {
        if (StringUtils.checkStringIsEmpty(destination)) {
            return "destination can not be empty";
        }
        if (this.destinationMap.containsKey(destination)) {
            return "ERROR : Destination : " + destination + " already has been created. Please remove it first. ";
        }
        ContentBaseDestinationBean destinationBean = new ContentBaseDestinationBean();
        destinationBean.setDestination(destination);
        destinationBean.setUniqueId(this.uniqueId);
        destinationBean.setAdministrationService(this.administrationService);
        this.destinationMap.put(destinationBean.getDestination(), destinationBean);
        this.administrationService.registerBean(destinationBean);
        return "destination : " + destination + " created successfully.";
    }

    // @BeanId
    @Override
    @Attribute
    public String getUniqueId() {
        return this.uniqueId;
    }
}
