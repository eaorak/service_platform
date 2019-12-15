package com.adenon.channel.sms.configuration.mapper;

import com.adenon.api.smpp.sdk.LogDescriptor;
import com.adenon.api.smpp.sdk.LogType;
import com.adenon.channel.sms.configuration.beans.LoggerBean;


public class LogDescriptorMapper {

    public static LogDescriptor get(final LoggerBean smsApiLogBean) {
        final LogDescriptor logDescriptor = new LogDescriptor();

        logDescriptor.setLevel(smsApiLogBean.getLog4JLevel(smsApiLogBean.getLogLevel()));
        logDescriptor.setLogType(LogType.getLogType(smsApiLogBean.getLogType()));
        logDescriptor.setWriteConsole(smsApiLogBean.getWriteConsole());
        return logDescriptor;
    }
}
