package com.adenon.sp.channels.api;

import java.util.concurrent.TimeUnit;

import com.adenon.sp.channels.channel.IChannelInfo;
import com.adenon.sp.kernel.dialog.Dialog;
import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.SubsystemType;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.osgi.IServices;
import com.adenon.sp.streams.IStreamService;


public final class ApiMessaging implements IApiMessaging {

    private static long          DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(10);
    // Services
    private final IChannelInfo   info;
    private final IStreamService streamService;
    // Parameters
    private final SubsystemType  originator      = SubsystemType.SYSTEM;
    private final Direction      direction       = Direction.TOWARDS_OUT;
    private final SubsystemType  destination;

    public ApiMessaging(IChannelInfo info,
                        IServices services) {
        this.streamService = services.getService(IStreamService.class);
        this.info = info;
        this.destination = info.getSubsystem();
    }

    @Override
    public Event begin(IMessage message,
                       DialogType type) throws Exception {
        return this.begin(message, type, null);
    }

    @Override
    public Event begin(IMessage message,
                       DialogType type,
                       IDialogHandler handler) throws Exception {
        Event event = Dialog.beginEvent(message, this.originator, this.destination, this.direction, this.info.getId(), type, DEFAULT_TIMEOUT);
        IDialog dialog = event.getDialog();
        dialog.setHandler(handler);
        // dialog.setEndpoint(returnService.getName());
        return this.sendEvent(event);
    }

    @Override
    public Event continuee(IDialog dialog,
                           IMessage message) throws Exception {
        Event event = dialog.continuee(message, this.direction);
        return this.sendEvent(event);
    }

    @Override
    public Event end(IDialog dialog,
                     IMessage message) throws Exception {
        Event event = dialog.end(message, this.direction);
        return this.sendEvent(event);
    }

    private Event sendEvent(Event event) throws Exception {
        this.streamService.execute(event, false);
        return event;
    }

    // **************************************************

    @Override
    public void sendStatelessMessage(IMessage message) throws Exception {
        this.sendMessage(message, null, DialogType.STATELESS, null);
    }

    @Override
    public void sendStatelessMessage(IMessage message,
                                     Class<?> returnService) throws Exception {
        this.sendMessage(message, null, DialogType.STATELESS, returnService);
    }

    @Override
    public void sendStatefulMessage(IMessage message,
                                    IDialogHandler handler) throws Exception {
        this.sendMessage(message, handler, DialogType.STATEFUL, null);
    }

    private void sendMessage(IMessage message,
                             IDialogHandler handler,
                             DialogType dialogType,
                             Class<?> returnService) throws Exception {
        Event beginEvent = Dialog.beginEvent(message, this.originator, this.destination, this.direction, this.info.getId(), dialogType, DEFAULT_TIMEOUT);
        IDialog dialog = beginEvent.getDialog();
        dialog.setHandler(handler);
        if (returnService != null) {
            dialog.setEndpoint(returnService.getName());
        }
        this.streamService.execute(beginEvent, true);
    }

}
