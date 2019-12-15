package com.adenon.sp.channels.messaging;

import com.adenon.sp.channels.ChannelInfo;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.channels.registry.ChannelRegistry;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.MessageType;
import com.adenon.sp.streams.IEventProcessor;


public class ChannelsEventProcessor implements IEventProcessor {

    private final ChannelRegistry registry;

    public ChannelsEventProcessor(ChannelRegistry registry) {
        this.registry = registry;
    }

    @Override
    public IError process(Event event) throws Exception {
        int enablerId = event.getDialog().getEnablerId();
        ChannelInfo enabler = this.registry.findById(enablerId);
        if (enabler == null) {
            throw new Exception("No enabler could be found with id : " + enablerId + " for event " + event);
        }
        IChannelProtocol enablerMessaging = enabler.getProtocol();
        MessageType type = event.getMessage().getType();
        enabler.getCounters().get(Direction.TOWARDS_OUT, type).increase();
        switch (type) {
            case BEGIN:
                enablerMessaging.receiveBegin(event);
                break;
            case CONTINUE:
                enablerMessaging.receiveContinue(event);
                break;
            case END:
                enablerMessaging.receiveEnd(event);
                break;
            case TERMINATE:
            default:
                enablerMessaging.receiveFail(event);
        }
        return null;
    }

    @Override
    public boolean failOnError() {
        return true;
    }


}
