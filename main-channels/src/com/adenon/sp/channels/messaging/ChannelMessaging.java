package com.adenon.sp.channels.messaging;

import com.adenon.sp.channels.channel.IChannelInfo;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.channels.registry.ChannelCounters;
import com.adenon.sp.kernel.dialog.Dialog;
import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.SubsystemType;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.MessageType;
import com.adenon.sp.streams.IEventProcessor;
import com.adenon.sp.streams.IStreamService;


public class ChannelMessaging implements IChannelMessaging {

    private final IChannelInfo    enabler;
    private final IStreamService  streamService;
    private final IEventProcessor dispatcher;
    private final ChannelCounters counters;

    public ChannelMessaging(IChannelInfo enabler,
                            IStreamService orchService,
                            IEventProcessor dispatcher,
                            ChannelCounters counters) {
        this.enabler = enabler;
        this.streamService = orchService;
        this.dispatcher = dispatcher;
        this.counters = counters;
    }

    @Override
    public Event begin(IMessage message,
                       DialogType status,
                       long lifeTime) {
        this.increaseCounter(message.getType());
        Event beginEvent = Dialog.beginEvent(message,
                                             this.enabler.getSubsystem(),
                                             SubsystemType.SYSTEM,
                                             Direction.TOWARDS_IN,
                                             this.enabler.getId(),
                                             status,
                                             lifeTime);
        try {
            this.dispatcher.process(beginEvent);
            this.sendMessage(beginEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return beginEvent;
    }

    @Override
    public Event continuee(IDialog dialog,
                           IMessage message) {
        this.increaseCounter(message.getType());
        Event continuee = dialog.continuee(message, Direction.TOWARDS_IN);
        this.sendMessage(continuee);
        return continuee;
    }

    @Override
    public Event end(IDialog dialog,
                     IMessage message) {
        this.increaseCounter(message.getType());
        Event end = dialog.end(message, Direction.TOWARDS_IN);
        this.sendMessage(end);
        return end;
    }

    private void sendMessage(Event event) {
        try {
            this.streamService.execute(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void increaseCounter(MessageType type) {
        this.counters.get(Direction.TOWARDS_IN, type).increase();
    }

}
