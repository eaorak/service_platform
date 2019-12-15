package com.adenon.library.common.request.response;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.adenon.sp.channels.api.IApiMessaging;
import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.execution.IRequest;



public abstract class SyncRequestResponse {


    private final IApiMessaging apimessaging;
    private final Logger        logger;
    private final long          timeout;

    public SyncRequestResponse(IApiMessaging apiMessaging,
                               long timeout,
                               Logger logger) {
        this.apimessaging = apiMessaging;
        this.timeout = timeout;
        this.logger = logger;
    }

    public <T, M extends IMessage & IResponseHelper> T requestWaitReaponse(final Object waitObject,
                                                                           final M requestMessage) {

        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        final IDialogHandler dialogHandler = new IDialogHandler() {

            @Override
            public void execute(IRequest request) throws Exception {
                T message = (T) request.getMessage();
                requestMessage.setResponse(message);
                synchronized (waitObject) {
                    waitObject.notify();
                }
            }

            @Override
            public void terminate(IRequest request,
                                  IError e) {
                SyncRequestResponse.this.teminated(e);
                synchronized (waitObject) {
                    waitObject.notify();
                    atomicBoolean.set(true);
                }
            }
        };
        synchronized (waitObject) {
            try {
                this.apimessaging.begin(requestMessage, DialogType.STATEFUL, dialogHandler);
                if (!atomicBoolean.get()) {
                    try {
                        waitObject.wait(this.timeout);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                this.logger.error("SyncRequestResponse->requestLocation-> Error : " + e.getMessage(), e);
            }
        }
        return requestMessage.getResponse();
    }

    public abstract void teminated(IError error);
}
