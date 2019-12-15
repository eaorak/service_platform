package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.Message;
import com.adenon.sp.kernel.event.message.MessageType;

@Message(messageType = MessageType.BEGIN, dispatchClassNames = { "com.adenon.sp.channels.dispatch.rules.DirectRouter" }, messageName = "DisconnectInformation")
public class SmsServerDisconnectMessage extends AbstractMessage {

    private String username;
    private String connectionName;

    public SmsServerDisconnectMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public SmsServerMessageTypes getId() {
        return SmsServerMessageTypes.SERVER_MESSAGE_DISCONNECT;
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

}
