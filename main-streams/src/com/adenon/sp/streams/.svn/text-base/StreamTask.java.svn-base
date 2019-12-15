package com.adenon.sp.streams;

import java.util.Set;

import org.apache.log4j.Logger;

import com.adenon.sp.channels.IChannelRegistry;
import com.adenon.sp.channels.channel.IChannelInfo;
import com.adenon.sp.kernel.error.ErrorObject;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.streams.IEventProcessor;
import com.adenon.sp.streams.ProcessorWrapper;
import com.adenon.sp.streams.StreamErrors;


public class StreamTask implements Runnable {

    private static Logger       logger = Logger.getLogger(StreamTask.class);
    private final Event         event;
    private final StreamService stream;
    private final Services      services;

    public StreamTask(Services services,
                      Event event,
                      StreamService flowService) {
        this.services = services;
        this.event = event;
        this.stream = flowService;
    }

    // TODO : What should happen when executed synchoronously ?
    @Override
    public void run() {
        process(this.services, this.stream, this.event);
    }

    public static boolean process(Services services,
                                  StreamService stream,
                                  Event event) {
        Direction direction = event.getDirection();
        Set<ProcessorWrapper> processors = stream.getProcessors(direction);
        for (IEventProcessor processor : processors) {
            IError error = null;
            try {
                error = processor.process(event);
            } catch (Throwable e) {
                logger.error("Error : " + e.getMessage(), e);
                error = ErrorObject.create(StreamErrors.STREAM_PROCESS_ERROR, e);
            } finally {
                if (error == null) {
                    continue;
                }
                handleFail(services, event, error);
                if (processor.failOnError()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void handleFail(Services services,
                                   Event event,
                                   IError error) {
        try {
            Direction direction = event.getDirection();
            switch (direction) {
                case TOWARDS_IN:
                    int enablerId = event.getDialog().getEnablerId();
                    IChannelRegistry registry = services.getService(IChannelRegistry.class);
                    IChannelInfo enabler = registry.findById(enablerId);
                    if (enabler == null) {
                        return;
                    }
                    enabler.getProtocol().receiveFail(event);
                    break;
                case TOWARDS_OUT:
                    event.getDialog().getHandler().terminate(event, error);
                    break;
                default:
                    break;
            }
        } catch (Throwable e) {
            logger.error("[StreamTask][handleFail] : Error : " + e.getMessage(), e);
        }
    }
}
