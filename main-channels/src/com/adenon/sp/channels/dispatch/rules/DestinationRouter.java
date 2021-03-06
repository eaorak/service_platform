package com.adenon.sp.channels.dispatch.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adenon.library.common.utils.StringUtils;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.channels.configuration.MessageBean;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.AbstractDefaultMessage;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.rule.EDispatchRuleResult;
import com.adenon.sp.kernel.event.message.rule.IDispatchRule;


@MBean(parent = MessageBean.class, subLocation = "destinationRouter=DestinationRouter", persist = true)
public class DestinationRouter extends Router implements IDispatchRule {

    private String                             messageName;
    private String                             uniqueId;
    private final Map<String, DestinationBean> destinationMap = new HashMap<String, DestinationBean>();
    private IAdministrationService             administrationService;


    public DestinationRouter() {
    }

    @Override
    public EDispatchRuleResult executeRule(Event event) throws Exception {
        IMessage message = event.getMessage();
        if (message instanceof AbstractDefaultMessage) {
            AbstractDefaultMessage abstractDefaultMessage = (AbstractDefaultMessage) message;
            String destination = abstractDefaultMessage.getDestination();
            DestinationBean destinationBean = this.destinationMap.get(destination);
            if (destinationBean != null) {
                if ((destinationBean.getServiceName() != null) && !"".equals(destinationBean.getServiceName())) {
                    event.getDialog().setEndpoint(destinationBean.getServiceName());
                    return EDispatchRuleResult.END;
                }
            }
        } else {
            throw new RuntimeException("Message : " + message + " should extend AbstractDefaultMessage");
        }
        // if (!message.getClass().isAssignableFrom(AbstractDefaultMessage.class)) {
        //
        // }
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
        IBeanHelper<DestinationBean> destinationBeansHelper = this.administrationService.getBeans(DestinationBean.class);
        IBeanHelper<DestinationBean> childsOf = destinationBeansHelper.getChildsOf(this);
        List<DestinationBean> allBeans = childsOf.getAllBeans();
        childsOf.register();
        for (DestinationBean destinationBean : allBeans) {
            this.destinationMap.put(destinationBean.getDestination(), destinationBean);
        }
    }

    // @BeanId
    @Override
    @Attribute(readOnly = true)
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Operation
    public String addDestination(@Parameter(name = "destination") final String destination,
                                 @Parameter(name = "serviceName") String serviceName) throws Exception {
        if (StringUtils.checkStringIsEmpty(destination)) {
            return "destination can not be empty";
        }
        if (StringUtils.checkStringIsEmpty(serviceName)) {
            return "serviceName can not be empty";
        }
        if (this.destinationMap.containsKey(destination)) {
            return "ERROR : Destination : " + destination + " already has been created. Please remove it first. ";
        }
        DestinationBean destinationBean = new DestinationBean();
        destinationBean.setDestination(destination);
        destinationBean.setServiceName(serviceName);
        destinationBean.setUniqueId(this.uniqueId);
        this.destinationMap.put(destinationBean.getDestination(), destinationBean);
        this.administrationService.registerBean(destinationBean);
        return "destination : " + destination + " serviceName : " + serviceName + " created successfully.";

    }


    @Operation
    public String removeDestination(@Parameter(name = "destination") final String destination) throws Exception {
        if (StringUtils.checkStringIsEmpty(destination)) {
            return "destination can not be empty";
        }
        DestinationBean removedBean = this.destinationMap.remove(destination);
        if (removedBean == null) {
            return "ERROR : Could not find the destination : " + destination;
        }
        this.administrationService.unregisterBean(removedBean);
        return "destination : " + destination + " removed successfully";
    }

}
