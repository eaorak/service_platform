package com.adenon.sp.channel.test;

import org.apache.log4j.Logger;

import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.event.Event;


public class MessageProcessor implements IChannelProtocol {

    private static final Logger logger = Logger.getLogger(MessageProcessor.class);

    public MessageProcessor() {
    }

    @Override
    public void receiveBegin(Event event) {
        if (logger.isInfoEnabled()) {
            logger.info("Begin: " + event);
        }
    }

    @Override
    public void receiveContinue(Event event) {
        if (logger.isInfoEnabled()) {
            logger.info("Continue: " + event);
        }
    }

    @Override
    public void receiveEnd(Event event) {
        if (logger.isInfoEnabled()) {
            logger.info("End: " + event);
        }
    }

    @Override
    public void receiveFail(Event event) {
        if (logger.isInfoEnabled()) {
            logger.info("Fail: " + event);
        }
    }

}
