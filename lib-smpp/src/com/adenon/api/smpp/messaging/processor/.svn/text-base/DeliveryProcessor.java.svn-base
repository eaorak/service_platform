package com.adenon.api.smpp.messaging.processor;

import java.nio.ByteBuffer;

import com.adenon.api.smpp.core.ResponseHandler;


public class DeliveryProcessor extends ResponseHandler {


    public DeliveryProcessor() {
        this.createHandler(1);
    }

    @Override
    public void fillMessageBody(ByteBuffer buffer,
                                int index,
                                byte[] concatHeader) throws Exception {
        buffer.put((byte) 0);
        return;

    }

}
