package com.adenon.channel.sms.configuration.beans;

import java.util.Hashtable;

import com.adenon.channel.sms.SmsActivator;
import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;


@MBean(location = ConfigLocation.CHANNELS, subLocation = "SmsAdapter=SMS", persist = false)
public class TZeroBean {

    private SmsActivator                                 smsActivator;
    private final Hashtable<String, ConnectionGroupBean> connectionGroupTable = new Hashtable<String, ConnectionGroupBean>();

    public TZeroBean() {
    }

    public TZeroBean(final SmsActivator smsActivator) {
        this.setSmsActivator(smsActivator);
    }


    @Operation
    public String addConnectionGroup(final String connectionGroupName) throws Exception {
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
