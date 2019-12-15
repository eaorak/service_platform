package com.adenon.sp.channel.webservice;

import org.apache.log4j.Logger;

import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.event.Event;


public class WsChannelProtocol implements IChannelProtocol {

    private static final Logger logger = Logger.getLogger(WsChannelProtocol.class);

    public WsChannelProtocol() {
    }

    @Override
    public void receiveBegin(Event event) {
        throw new RuntimeException("Wrong message flow !");
    }

    @Override
    public void receiveContinue(Event event) {
        throw new RuntimeException("Wrong message flow !");
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
