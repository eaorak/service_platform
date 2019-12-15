package com.adenon.channel.sms.configuration.mapper;

import java.util.StringTokenizer;

import com.adenon.api.smpp.sdk.ConnectionDescriptor;
import com.adenon.api.smpp.sdk.SmppConnectionType;
import com.adenon.channel.sms.configuration.beans.ConnectionBean;


public class ConnectionDescriptorMapper {

    public static ConnectionDescriptor get(final ConnectionBean connectionBean) {
        final ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor(connectionBean.getConnectionGroupName(), connectionBean.getConnectionName());
        connectionDescriptor.setUsername(connectionBean.getUsername());
        connectionDescriptor.setPassword(connectionBean.getPassword());
        connectionDescriptor.setPort(connectionBean.getPort());
        connectionDescriptor.setWindowSize(connectionBean.getSmppWindowSize());
        connectionDescriptor.setConnectionType(SmppConnectionType.getLogType(connectionBean.getConnectionType()));
        connectionDescriptor.setTraceON(connectionBean.getTrace());
        connectionDescriptor.setMaxThreadCount(connectionBean.getMaxThreadCount());
        if (connectionBean.getConnectionIpList() != null) {
            final StringTokenizer stringTokenizer = new StringTokenizer(connectionBean.getConnectionIpList(), ",");
            while (stringTokenizer.hasMoreTokens()) {
                final String nextToken = stringTokenizer.nextToken();
                connectionDescriptor.addIp(nextToken);
            }
        }

        return connectionDescriptor;
    }

}
