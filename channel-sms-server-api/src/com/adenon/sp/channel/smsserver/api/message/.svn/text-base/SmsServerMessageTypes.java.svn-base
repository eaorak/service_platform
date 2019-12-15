package com.adenon.sp.channel.smsserver.api.message;

import com.adenon.sp.kernel.event.message.IMessageTypes;


public enum SmsServerMessageTypes implements IMessageTypes {

    SERVER_MESSAGE_BIND(1),
    SERVER_MESSAGE_DISCONNECT(2),
    SERVER_MESSAGE_SUBMIT(3),
    SERVER_MESSAGE_DELIVERY(4),
    SERVER_MESSAGE_DELIVERSM(5),
    SERVER_MESSAGE_DELIVER_RESPONSE(6);

    private int id;

    private SmsServerMessageTypes(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }

}
