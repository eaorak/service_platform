package com.adenon.sp.channels.api;

public abstract class AbstractChannelProvider {

    protected final IApiMessaging messaging;

    public AbstractChannelProvider(IApiMessaging messaging) {
        this.messaging = messaging;
    }

}
