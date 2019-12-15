package com.adenon.library.common.request.response;

import java.util.HashMap;
import java.util.Map;

import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.MessageType;



public class RequestResponse {

    private IChannelMessaging                  channelMessaging;

    private final Map<String, Container<?, ?>> containerMap = new HashMap<String, Container<?, ?>>();

    private final long                         timeout;


    public RequestResponse(IChannelMessaging channelMessaging,
                           long timeout) {
        this.channelMessaging = channelMessaging;
        this.timeout = timeout;
    }

    public <K extends IMessage, V extends IMessage> Container<K, V> waitForResponse(String id,
                                                                                    K request,
                                                                                    V response) {
        Container<K, V> container = new Container<K, V>();
        Container oldContainer = null;
        IDialog dialog = null;
        if (MessageType.BEGIN != request.getType()) {
            oldContainer = this.containerMap.remove(id);
            if (oldContainer != null) {
                dialog = oldContainer.getDialog();
            } else {
                container.setErrorOccured(true);
                container.setErrorDescription("Dialog is empty");
                container.setErrorCode(Container.ERROR_RESPONSE_DIALOG_EMPTY);
                return null;

            }
        }
        Object waitObj = new Object();
        container.setWaitObject(waitObj);
        container.setRequestObject(request);
        container.setResponseObject(response);
        container.setDialog(dialog);
        this.containerMap.put(id, container);
        synchronized (waitObj) {
            switch (request.getType()) {
                case BEGIN:
                    Event begin = this.channelMessaging.begin(request, DialogType.STATEFUL, this.timeout);
                    container.setDialog(begin.getDialog());
                    break;
                case CONTINUE:
                    this.channelMessaging.continuee(container.getDialog(), request);
                    break;
                case END:
                    this.channelMessaging.end(container.getDialog(), request);
                    break;
            }
            try {
                waitObj.wait(this.timeout);
            } catch (Exception e) {
            }
        }

        if (container != null) {
            if (!container.isResponseReceived()) {
                container.setErrorOccured(true);
                container.setErrorDescription("Reponse expired!!!");
                container.setErrorCode(Container.ERROR_RESPONSE_EXPIRED);
            }
        }
        return container;
    }

    public <K extends IMessage, V extends IMessage> Container<K, V> notifyRequest(String id,
                                                                                  V responseMessage) {
        Container<K, V> container = null;
        if (responseMessage == null) {
            container.setResponseReceived(true);
            container.setErrorOccured(true);
            container.setErrorCode(Container.ERROR_RESPONSE_ISNULL);
            container.setErrorDescription("Response object is null!");
            return container;
        }
        switch (responseMessage.getType()) {
            case CONTINUE:
                container = (Container<K, V>) this.containerMap.get(id);
                break;
            case END:
            case TERMINATE:
                container = (Container<K, V>) this.containerMap.remove(id);
                break;
        }

        if (container != null) {
            container.setResponseObject(responseMessage);
            Object waitObject = container.getWaitObject();
            container.setResponseReceived(true);
            synchronized (waitObject) {
                waitObject.notifyAll();
            }
        }
        return container;
    }

}
