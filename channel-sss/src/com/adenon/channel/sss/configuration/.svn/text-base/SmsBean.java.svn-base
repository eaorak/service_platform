package com.adenon.channel.sss.configuration;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.kernel.osgi.Services;


@MBean(location = ConfigLocation.CHANNELS, subLocation = "SmsAdapter=SMS", persist = false)
public class SmsBean {

    private final Map<String, SmsConnectionGroup> connections = new Hashtable<String, SmsConnectionGroup>();
    private IAdministrationService                administration;
    private Services                              services;

    @Deprecated
    public SmsBean() {
    }

    public SmsBean(Services services) {
        this.services = services;
        this.administration = services.getService(IAdministrationService.class);
    }

    public void initialize(List<SmsConnectionGroup> smsGroups) {
        for (SmsConnectionGroup group : smsGroups) {
            this.connections.put(group.getName(), group);
        }
    }

    @Operation(name = "Add Connection Group")
    public String addConnectionGroup(@Parameter(name = "Connection Group") final String groupName) throws Exception {
        if (this.connections.containsKey(groupName)) {
            return "Connection group name : " + groupName + " has already been created. Please remove first.";
        }
        final SmsConnectionGroup connGroup = new SmsConnectionGroup(this.services);
        connGroup.setName(groupName);
        this.connections.put(groupName, connGroup);
        this.administration.registerBean(connGroup);
        return "Connection group name : " + groupName + " has been sucessfully created. ";
    }

}
