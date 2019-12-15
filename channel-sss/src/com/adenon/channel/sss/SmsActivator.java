package com.adenon.channel.sss;

import com.adenon.channel.sss.api.operation.ISmsProvider;
import com.adenon.channel.sss.configuration.SmsBean;
import com.adenon.channel.sss.configuration.SmsConnection;
import com.adenon.channel.sss.configuration.SmsConnectionGroup;
import com.adenon.channel.sss.configuration.SmsProvider;
import com.adenon.sp.administration.AdministrationException;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.channels.channel.ChannelActivator;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.event.SubsystemType;


public class SmsActivator extends ChannelActivator {

    @Override
    protected void startChannel() throws Exception {
        IAdministrationService administration = this.getService(IAdministrationService.class);
        this.initializeConfiguration(administration);
        this.registerService(ISmsProvider.class, new SmsProvider(this.apiMessaging));
    }

    private void initializeConfiguration(IAdministrationService administration) throws AdministrationException {
        // Sms Bean
        SmsBean smsBean = new SmsBean(this.services);
        administration.registerBean(smsBean);
        // All Group Beans
        IBeanHelper<SmsConnectionGroup> connectionGroups = administration.getBeans(SmsConnectionGroup.class);
        // All Connection Beans
        IBeanHelper<SmsConnection> connections = administration.getBeans(SmsConnection.class);
        for (SmsConnectionGroup group : connectionGroups.register()) {
            // Find connections of groups
            IBeanHelper<SmsConnection> groupConnections = connections.getChildsOf(group);
            group.initialize(this.services, groupConnections.register());
        }
        smsBean.initialize(connectionGroups.getAllBeans());
    }

    @Override
    protected void stopChannel() throws Exception {
    }

    @Override
    protected IChannelProtocol getProtocol() {
        return new SssProtocol(this.channelMessaging);
    }

    @Override
    protected SubsystemType getSubsystem() {
        return SubsystemType.NETWORK;
    }

    @Override
    protected String getShortName() {
        return "SSS";
    }

}
