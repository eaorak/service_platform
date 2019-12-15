package com.adenon.api.smpp.sdk;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.connection.SmppConnectionGroupManager;
import com.adenon.api.smpp.connection.SmppConnectionLocator;
import com.adenon.api.smpp.core.SmppIOReactor;
import com.adenon.api.smpp.logging.LogManager;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.message.SubmitSMMessage;


public class SmppMessagingManager {

    private final LoggerWrapper         logger;

    private final LogManager            logManager;
    private final SmppConnectionLocator smppConnectionLocator;

    public SmppMessagingManager(final SmppConnectionGroupManager pConnectionManager,
                                final LogManager pLogManager,
                                final SmppConnectionLocator pSmppConnectionLocator) {
        this.smppConnectionLocator = pSmppConnectionLocator;
        this.logManager = pLogManager;
        this.logger = this.logManager.getLogControler().getLogger();

    }

    public ISMSSendResult syncSendSms(ConnectionInfo connectionInfo,
                                      IMessage message,
                                      long transactionId,
                                      AddressDescriptor destinationAddress,
                                      AddressDescriptor originatingAddress,
                                      AdditionalParamatersDescriptor paramatersDescriptor,
                                      Object returnObject,
                                      long waitTimeout) {

        if (waitTimeout < 10) {
            return new SendResult(ISendResult.ERROR_CAUSE_INVALID, "Wait timeout should be more than 10 ms !!!");
        }
        return this.sendSms(connectionInfo, message, transactionId, destinationAddress, originatingAddress, paramatersDescriptor, returnObject, waitTimeout);
    }

    public ISMSSendResult sendSms(ConnectionInfo connectionInfo,
                                  IMessage message,
                                  long transactionId,
                                  AddressDescriptor destinationAddress,
                                  AddressDescriptor originatingAddress,
                                  AdditionalParamatersDescriptor paramatersDescriptor,
                                  Object returnObject,
                                  long waitTimeout) {
        SmppIOReactor ioReactor = null;
        try {
            if (connectionInfo == null) {
                ioReactor = this.smppConnectionLocator.findAvaliableClientConnection(null, null);
            } else {
                if (connectionInfo.checkConnectionInfoIsNull()) {
                    ioReactor = this.smppConnectionLocator.findAvaliableClientConnection(null, null);
                } else {
                    ioReactor = this.smppConnectionLocator.findAvaliableClientConnection(connectionInfo.getConnectionGroupName(),
                                                                                         connectionInfo.getConnectionName());
                }
            }

        } catch (final SmppApiException e) {
            this.logger.error("MessagingManager", "sendSms", 0, null, " : Error : " + e.getMessage(), e);
            return new SendResult(ISendResult.ERROR_CAUSE_INTERNAL_ERROR, "Error occured while getting a valid connection. Desc : " + e.getMessage());
        } catch (Exception e) {
            this.logger.error("MessagingManager", "sendSms", 0, null, " : Error : " + e.getMessage(), e);
            return new SendResult(ISendResult.ERROR_CAUSE_FATAL_ERROR, " Fatal Error : " + e.getMessage());
        }
        if (ioReactor == null) {
            if (connectionInfo == null) {
                this.logger.error("MessagingManager", "sendSms", 0, null, "Could not find free connected connection.");
            } else {
                this.logger.error("MessagingManager",
                                  "sendSms",
                                  0,
                                  null,
                                  "Could not find free connected connection group: "
                                          + connectionInfo.getConnectionGroupName()
                                          + " connection : "
                                          + connectionInfo.getConnectionName());
            }
            return new SendResult(ESendResult.RETRY, ISendResult.ERROR_CAUSE_NO_CONNECTED_CONNECTION, "There is no valid connection");
        }
        return this.sendSms(ioReactor, message, transactionId, destinationAddress, originatingAddress, paramatersDescriptor, returnObject, waitTimeout);
    }

    private ISMSSendResult sendSms(SmppIOReactor ioReactor,
                                   IMessage message,
                                   long transactionId,
                                   AddressDescriptor destinationAddress,
                                   AddressDescriptor originatingAddress,
                                   AdditionalParamatersDescriptor paramatersDescriptor,
                                   Object returnObject,
                                   long waitTimeout) {
        if ((ioReactor.getConnectionDescriptor().getConnectionType() != SmppConnectionType.READ)) {
            SubmitSMMessage smpp34SubmitSM = null;
            if (transactionId < 0) {
                smpp34SubmitSM = ioReactor.createSubmitSMMessage();
            } else {
                smpp34SubmitSM = ioReactor.createSubmitSMMessage(transactionId);
            }

            final long transID = smpp34SubmitSM.getTransID();
            long retVal = 0;
            try {
                smpp34SubmitSM.setMessage(message);
                smpp34SubmitSM.setDestinationAddress(destinationAddress);
                smpp34SubmitSM.setSourceAddress(originatingAddress);
                boolean requestDelivery = false;
                boolean putConcatHeader = false;
                if (paramatersDescriptor != null) {
                    if (paramatersDescriptor.isRequestDelivery()) {
                        requestDelivery = true;
                    }
                    if (paramatersDescriptor.isPutConcatHeader()) {
                        putConcatHeader = true;
                    }
                    if (paramatersDescriptor.getValidityPeriod() > 0) {
                        smpp34SubmitSM.setParamValidityPeriod(CommonUtils.relativeTimeStringFromSeconds(paramatersDescriptor.getValidityPeriod()));
                    }
                    if (paramatersDescriptor.getMessageSchedulePeriod() > 0) {
                        smpp34SubmitSM.setParamScheduleDeliveryTime(CommonUtils.relativeTimeStringFromMinutes(paramatersDescriptor.getMessageSchedulePeriod()));
                    }
                }

                if (waitTimeout > 0) {
                    Object waitObject = new Object();
                    smpp34SubmitSM.setWaitObject(waitObject);
                    try {
                        synchronized (waitObject) {
                            retVal = ioReactor.sendSubmitSM(smpp34SubmitSM, putConcatHeader, requestDelivery, returnObject);
                            waitObject.wait(waitTimeout);
                        }
                    } catch (SmppApiException exp) {
                        this.logger.error("SmppMessagingManager",
                                          "sendSms",
                                          transactionId,
                                          ioReactor.getLabel(),
                                          " : SMPP API Exception Error : " + exp.getMessage(),
                                          exp);
                        return new SendResult(exp.getErrorCode(), "SMPP API  Exception Error : " + exp.getMessage());
                    } catch (Exception e) {
                        this.logger.error("SmppMessagingManager", "sendSms", transactionId, ioReactor.getLabel(), " : Error : " + e.getMessage(), e);
                        return new SendResult(ISendResult.ERROR_CAUSE_FATAL_ERROR, "System error : " + e.getMessage());
                    }
                } else {
                    retVal = ioReactor.sendSubmitSM(smpp34SubmitSM, putConcatHeader, requestDelivery, returnObject);
                }
            } catch (final Exception e) {
                this.logger.error("MessagingManager", "sendSms", transID, ioReactor.getLabel(), " : Error : " + e.getMessage(), e);
                return new SendResult(ISendResult.ERROR_CAUSE_FATAL_ERROR, "System error : " + e.getMessage());
            }
            if (retVal != 0) {
                SendResult mySendResult = new SendResult(ioReactor.getConnectionInformation(), transactionId, smpp34SubmitSM);
                ESendResult submitSMsendResult = smpp34SubmitSM.getSendResult();
                mySendResult.setSendResult(submitSMsendResult);
                return mySendResult;
            } else {
                return new SendResult(ISendResult.ERROR_CAUSE_FATAL_ERROR, "System error : return value should be greater than 0");
            }
        } else {
            this.logger.error("MessagingManager", "sendSms", 0, ioReactor.getLabel(), " Sending message is not allowed through this connection.");
            return new SendResult(ISendResult.ERROR_CAUSE_CONNECTION_READONLY, "Sending message is not allowed through this connection : "
                                                                               + ioReactor.getLabel());
        }
    }
}
