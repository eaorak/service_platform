package com.adenon.channel.sms.configuration.beans;

import java.util.Hashtable;

import com.adenon.channel.sms.SmsActivator;
import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;


@MBean(location = ConfigLocation.CHANNELS, subLocation = "SmsAdapter=SMS", persist = false)
public class SmsBean {

    private SmsActivator                                 smsActivator;
    private final Hashtable<String, ConnectionGroupBean> connectionGroupTable = new Hashtable<String, ConnectionGroupBean>();

    public SmsBean() {
    }

    public SmsBean(final SmsActivator smsActivator) {
        this.setSmsActivator(smsActivator);
    }


    @Operation(name = "Add Connection Group")
    public String addConnectionGroup(@Parameter(name = "Connection Group") String connectionGroupName) throws Exception {
        if (this.connectionGroupTable.containsKey(connectionGroupName)) {
            return "Connection group name : " + connectionGroupName + " has already been created. Please remove first.";
        }

        final ConnectionGroupBean connectionGroupBean = new ConnectionGroupBean(this.getSmsActivator());
        connectionGroupBean.setConnectionGroupName(connectionGroupName);

        this.connectionGroupTable.put(connectionGroupName, connectionGroupBean);

        this.getSmsActivator().getConfigurationService().registerBean(connectionGroupBean);

        return "Connection group name : " + connectionGroupName + " has been sucessfully created. ";
    }

    public SmsActivator getSmsActivator() {
        return this.smsActivator;
    }

    public void setSmsActivator(final SmsActivator smsActivator) {
        this.smsActivator = smsActivator;
    }
}
