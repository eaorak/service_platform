package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.sp.kernel.event.message.AbstractDefaultMessage;
import com.adenon.sp.kernel.event.message.Message;
import com.adenon.sp.kernel.event.message.MessageType;

@Message(messageType = MessageType.BEGIN, dispatchClassNames = {
                                                                "com.adenon.sp.channels.dispatch.rules.DirectRouter",
                                                                "com.adenon.sp.channels.dispatch.rules.DestinationRouter" }, messageName = "BindInformation")
public class SmsServerBindInfoMessage extends AbstractDefaultMessage {

    private String username;
    private String connectionName;
    private String ip;

    public SmsServerBindInfoMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsServerMessageTypes getId() {
        return SmsServerMessageTypes.SERVER_MESSAGE_BIND;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConnectionName() {
        return this.connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    @Override
    public String getSource() {
        return this.ip;
    }

    @Override
    public String getDestination() {
        return this.username;
    }

    @Override
    public String getMessageString() {
        return null;
    }

    @Override
    public String getMessageArgument() {
        return null;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
