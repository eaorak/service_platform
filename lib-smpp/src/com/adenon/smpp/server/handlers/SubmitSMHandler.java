package com.adenon.smpp.server.handlers;

import java.nio.ByteBuffer;

import com.adenon.api.smpp.message.MessageHeader;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.smpp.server.callback.response.ESubmitResult;
import com.adenon.smpp.server.callback.response.SubmitResponse;
import com.adenon.smpp.server.core.ServerIOReactor;


public class SubmitSMHandler {

    public void handle(final MessageHeader header,
                       final ByteBuffer byteBuffer,
                       final ServerIOReactor ioReactor) throws Exception {
        if (ioReactor.getLogger().isDebugEnabled()) {
            ioReactor.getLogger().debug("SubmitSMHandler",
                                        "handle",
                                        header.getSequenceNo(),
                                        ioReactor.getLabel(),
                                        " SubmitSM recieved sequence : " + header.getSequenceNo() + " . Handling SubmitSM.");
        }

        SubmitSMMessage submitSMMessage = new SubmitSMMessage(ioReactor.getLogger(), -1, ioReactor.getLabel());
        submitSMMessage.parseMessage(byteBuffer);
        SubmitResponse submitResponse = ioReactor.getServerCallback().submitSMReceived(ioReactor.getConnectionInformation(), submitSMMessage);

        if (submitResponse.getSubmitResult() != ESubmitResult.DoNothing) {
            ioReactor.sendSubmitSMResponse(header.getSequenceNo(), submitResponse.getMessageId(), byteBuffer, submitResponse.getSubmitResult().getValue());
            if (ioReactor.getLogger().isDebugEnabled()) {
                ioReactor.getLogger().debug("SubmitSMHandler",
                                            "handle",
                                            header.getSequenceNo(),
                                            ioReactor.getLabel(),
                                            " SubmitSM Response sent sequence : "
                                                    + header.getSequenceNo()
                                                    + " msgId : "
                                                    + submitResponse.getMessageId()
                                                    + " Status : "
                                                    + submitResponse.getSubmitResult().getValue()
                                                    + "-"
                                                    + submitResponse.getSubmitResult().getDescription());
            }

        }


    }

}
