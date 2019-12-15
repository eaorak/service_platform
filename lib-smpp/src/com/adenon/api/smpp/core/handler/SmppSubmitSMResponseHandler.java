package com.adenon.api.smpp.core.handler;

import java.nio.ByteBuffer;

import com.adenon.api.smpp.common.Smpp34ErrorCodes;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.core.SmppIOReactor;
import com.adenon.api.smpp.core.buffer.BufferBean;
import com.adenon.api.smpp.message.MessageHeader;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.api.smpp.message.SubmitSMResponseMessage;
import com.adenon.api.smpp.messaging.processor.IMessageProcessor;
import com.adenon.api.smpp.sdk.ESendResult;


public class SmppSubmitSMResponseHandler {

    public void handle(MessageHeader header,
                       SmppIOReactor smppIOReactor,
                       ByteBuffer byteBuffer) throws Exception {
        final SubmitSMResponseMessage submitSMResponse = new SubmitSMResponseMessage();
        BufferBean bufferBean;
        submitSMResponse.parseMessage(byteBuffer);
        bufferBean = smppIOReactor.getResponseBuffer().findItem(header.getSequenceNo());
        if (bufferBean != null) {
            SubmitSMMessage waitingSubmitSM = null;
            try {
                waitingSubmitSM = (SubmitSMMessage) bufferBean.getWaitingObject();
            } catch (final Exception e) {
                smppIOReactor.getLogger().error("SmppSubmitSMResponseHandler", "handle", 0, null, " : Error : " + e.getMessage(), e);
                return;
            } finally {
                bufferBean.release();
            }
            if (waitingSubmitSM == null) {
                smppIOReactor.getLogger().error("SmppSubmitSMResponseHandler", "handle", 0, null, " : Error : SubmitSM should not have been null!!");
                return;
            }
            if (smppIOReactor.getLogger().isDebugEnabled()) {
                smppIOReactor.getLogger().debug("SmppSubmitSMResponseHandler",
                                                "handle",
                                                waitingSubmitSM.getTransID(),
                                                smppIOReactor.getLabel(),
                                                " Msg Ref : " + submitSMResponse.getMessageIdentifier());
            }
            if (smppIOReactor.getConnectionInformation().getConnectionState().isSuspended()) {
                final int freeItemsCount = smppIOReactor.getResponseBuffer().getFreeItemCount();
                if (freeItemsCount > (smppIOReactor.getResponseBuffer().getBufferSize() / 2)) {
                    if (smppIOReactor.getLogger().isInfoEnabled()) {
                        smppIOReactor.getLogger().info("SmppSubmitSMResponseHandler",
                                                       "handle",
                                                       0,
                                                       smppIOReactor.getLabel(),
                                                       " : buffer free . Starting " + smppIOReactor.getLabel() + ". Sanity green.");
                    }
                    smppIOReactor.getConnectionInformation().getConnectionState().idle();
                }
            }
            final int errorNo = header.getCommandStatus();
            waitingSubmitSM.setServerErrorCode(errorNo);
            if (errorNo == Smpp34ErrorCodes.ERROR_CODE_ROK) {
                smppIOReactor.getStatisticCollector().increaseTotalReceivedSuccessfullSubmitSMCount();
                final IMessageProcessor messageProcessor = waitingSubmitSM.getMessageProcessor();
                boolean allResponseReceived = true;
                if (messageProcessor != null) {
                    allResponseReceived = messageProcessor.responseReceived(header.getSequenceNo(), submitSMResponse.getMessageIdentifier());
                }
                if (allResponseReceived) {
                    waitingSubmitSM.setSendResult(ESendResult.SUCCESS);
                    if (waitingSubmitSM.getWaitObject() == null) {
                        smppIOReactor.getSmppCallback().submitResult(smppIOReactor.getConnectionInformation(),
                                                                     waitingSubmitSM,
                                                                     waitingSubmitSM.getAttachedObject());
                    } else {
                        synchronized (waitingSubmitSM.getWaitObject()) {
                            waitingSubmitSM.getWaitObject().notify();
                        }
                    }

                }
                return;
            } else if ((errorNo == Smpp34ErrorCodes.ERROR_CODE_RSYSERR)
                       || (errorNo == Smpp34ErrorCodes.ERROR_CODE_RMSGQFUL)
                       || (errorNo == Smpp34ErrorCodes.ERROR_CODE_RTHROTTLED)) {
                smppIOReactor.getStatisticCollector().increaseTotalReceivedRetrySubmitSMCount();
                waitingSubmitSM.setSendResult(ESendResult.RETRY);
                final IMessageProcessor messageProcessor = waitingSubmitSM.getMessageProcessor();
                messageProcessor.errorReceived();
                if (waitingSubmitSM.getWaitObject() == null) {
                    smppIOReactor.getSmppCallback()
                                 .submitResult(smppIOReactor.getConnectionInformation(), waitingSubmitSM, waitingSubmitSM.getAttachedObject());
                } else {
                    synchronized (waitingSubmitSM.getWaitObject()) {
                        waitingSubmitSM.getWaitObject().notify();
                    }
                }

                return;
            } else if ((errorNo > Smpp34ErrorCodes.ERROR_CODE_ROK)
                       && (errorNo != Smpp34ErrorCodes.ERROR_CODE_RINVDSTADR)
                       && (errorNo != Smpp34ErrorCodes.ERROR_CODE_RCANCELFAIL)
                       && (errorNo < Smpp34ErrorCodes.ERROR_CODE_RMSGQFUL)) {
                smppIOReactor.getStatisticCollector().increaseTotalReceivedFailedSubmitSMCount();
                waitingSubmitSM.setSendResult(ESendResult.FATAL_ERROR);
                final IMessageProcessor messageProcessor = waitingSubmitSM.getMessageProcessor();
                messageProcessor.errorReceived();
                if (waitingSubmitSM.getWaitObject() == null) {
                    smppIOReactor.getSmppCallback()
                                 .submitResult(smppIOReactor.getConnectionInformation(), waitingSubmitSM, waitingSubmitSM.getAttachedObject());
                } else {
                    synchronized (waitingSubmitSM.getWaitObject()) {
                        waitingSubmitSM.getWaitObject().notify();
                    }
                }
                throw new SmppApiException(SmppApiException.FATAL_ERROR, SmppApiException.DOMAIN_SMSC, "From SMSC we received error code : "
                                                                                                       + (new Integer(errorNo)).toString());
            } else if ((errorNo == 11) || (errorNo >= 20)) {
                smppIOReactor.getStatisticCollector().increaseTotalReceivedFailedSubmitSMCount();
                waitingSubmitSM.setSendResult(ESendResult.ERROR);
                final IMessageProcessor messageProcessor = waitingSubmitSM.getMessageProcessor();
                messageProcessor.errorReceived();
                if (waitingSubmitSM.getWaitObject() == null) {
                    smppIOReactor.getSmppCallback()
                                 .submitResult(smppIOReactor.getConnectionInformation(), waitingSubmitSM, waitingSubmitSM.getAttachedObject());
                } else {
                    synchronized (waitingSubmitSM.getWaitObject()) {
                        waitingSubmitSM.getWaitObject().notify();
                    }
                }
                return;
            }
        } else {
            smppIOReactor.getLogger().error("SmppSubmitSMResponseHandler",
                                            "handle",
                                            0,
                                            smppIOReactor.getLabel(),
                                            "  Sequence is absent : " + header.getSequenceNo());
        }

    }
}
