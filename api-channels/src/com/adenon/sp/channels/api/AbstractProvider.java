package com.adenon.sp.channels.api;

public abstract class AbstractProvider {

    protected IApiMessaging messaging;

    public AbstractProvider(IApiMessaging messaging) {
        this.messaging = messaging;
    }

}
