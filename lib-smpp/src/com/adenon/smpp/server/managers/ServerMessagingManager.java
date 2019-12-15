package com.adenon.smpp.server.managers;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.EDeliveryStatus;
import com.adenon.api.smpp.sdk.IMessage;
import com.adenon.api.smpp.sdk.SmppConnectionType;
import com.adenon.smpp.server.core.DeliveryResult;
import com.adenon.smpp.server.core.EDeliveryResult;
import com.adenon.smpp.server.core.IDeliveryResult;
import com.adenon.smpp.server.core.ServerApiDelegator;
import com.adenon.smpp.server.core.ServerConnectionStore;
import com.adenon.smpp.server.core.ServerIOReactor;


public class ServerMessagingManager {


    private final ServerApiDelegator serverApiDelegator;
    private final LoggerWrapper      logger;


    public ServerMessagingManager(ServerApiDelegator serverApiDelegator) {
        this.serverApiDelegator = serverApiDelegator;
        this.logger = this.serverApiDelegator.getLogManager().getLogger();


    }

    public IDeliveryResult sendDelivery(String connectionName,
                                        String messageIdentifier,
                                        EDeliveryStatus deliveryStatus,
                                        long transactionId,
                                        AddressDescriptor destinationAddress,
                                        AddressDescriptor originatingAddress,
                                        AdditionalParamatersDescriptor paramatersDescriptor,
                                        Object attachedObject) {

        return this.sendDeliveryMessage(connectionName,
                                        messageIdentifier,
                                        true,
                                        deliveryStatus,
                                        null,
                                        transactionId,
                                        destinationAddress,
                                        originatingAddress,
                                        paramatersDescriptor,
                                        attachedObject,
                                        -1);
    }


    public IDeliveryResult syncSendDelivery(String connectionName,
                                            String messageIdentifier,
                                            EDeliveryStatus deliveryStatus,
                                            long transactionId,
                                            AddressDescriptor destinationAddress,
                                            AddressDescriptor originatingAddress,
                                            AdditionalParamatersDescriptor paramatersDescriptor,
                                            Object attachedObject,
                                            long waitTimeout) {
        return this.sendDeliveryMessage(connectionName,
                                        messageIdentifier,
                                        true,
                                        deliveryStatus,
                                        null,
                                        transactionId,
                                        destinationAddress,
                                        originatingAddress,
                                        paramatersDescriptor,
                                        attachedObject,
                                        waitTimeout);
    }


    public IDeliveryResult sendDeliverSM(String connectionName,
                                         IMessage messageDescriptor,
                                         long transactionId,
                                         AddressDescriptor destinationAddress,
                                         AddressDescriptor originatingAddress,
                                         AdditionalParamatersDescriptor paramatersDescriptor,
                                         Object attachedObject) {

        return this.sendDeliveryMessage(connectionName,
                                        null,
                                        false,
                                        null,
                                        messageDescriptor,
                                        transactionId,
                                        destinationAddress,
                                        originatingAddress,
                                        paramatersDescriptor,
                                        attachedObject,
                                        -1);
    }


    public IDeliveryResult syncSendDeliverSM(String connectionName,
                                             IMessage messageDescriptor,
                                             long transactionId,
                                             AddressDescriptor destinationAddress,
                                             AddressDescriptor originatingAddress,
                                             AdditionalParamatersDescriptor paramatersDescriptor,
                                             Object attachedObject,
                                             long waitTimeout) {
        return this.sendDeliveryMessage(connectionName,
                                        null,
                                        false,
                                        null,
                                        messageDescriptor,
                                        transactionId,
                                        destinationAddress,
                                        originatingAddress,
                                        paramatersDescriptor,
                                        attachedObject,
                                        waitTimeout);
    }

    private IDeliveryResult sendDeliveryMessage(String connectionName,
                                                String messageIdentifier,
                                                boolean isDelivery,
                                                EDeliveryStatus deliveryStatus,
                                                IMessage message,
                                                long transactionId,
                                                AddressDescriptor destinationAddress,
                                                AddressDescriptor originatingAddress,
                                                AdditionalParamatersDescriptor paramatersDescriptor,
                                                Object returnObject,
                                                long waitTimeout) {

        ServerConnectionStore serverConnectionStore = this.serverApiDelegator.getServerConnectionStore();
        ServerIOReactor ioReactor = serverConnectionStore.get(connectionName);
        if (ioReactor == null) {
            this.logger.error("ServerMessagingManager", "sendDeliverMessage", 0, null, " : No connection with given name : " + connectionName);
            DeliveryResult deliveryResult = new DeliveryResult(EDeliveryResult.DeliveryFailed,
                                                               IDeliveryResult.ERROR_CAUSE_NO_CONNECTION,
                                                               "There is no connection with given name.",
                                                               null,
                                                               transactionId);
            return deliveryResult;
        }

        if ((ioReactor.getBindType() == SmppConnectionType.BOTH) || (ioReactor.getBindType() == SmppConnectionType.READ)) {
            DeliverSMMessage deliverSMMessage = null;
            if (transactionId <= 0) {
                deliverSMMessage = ioReactor.createDeliverSMMessage();
            } else {
                deliverSMMessage = ioReactor.createDeliverSMMessage(transactionId);
            }

            final long transID = deliverSMMessage.getTransactionId();
            long retVal = 0;
            try {
                deliverSMMessage.setMessage(message);
                deliverSMMessage.setDestinationAddress(destinationAddress);
                deliverSMMessage.setSourceAddress(originatingAddress);
                if (isDelivery) {
                    deliverSMMessage.setOpParamMessageId(messageIdentifier);
                    deliverSMMessage.setOpParamMessageState(deliveryStatus.getValue());
                    deliverSMMessage.setDelivery(true);
                    deliverSMMessage.setParamESMClass((deliverSMMessage.getParamESMClass() | 0x00000004));
                }
                if (paramatersDescriptor != null) {
                    if (paramatersDescriptor.getValidityPeriod() > 0) {
                        deliverSMMessage.setParamValidityPeriod(CommonUtils.relativeTimeStringFromSeconds(paramatersDescriptor.getValidityPeriod()));
                    }
                    if (paramatersDescriptor.getMessageSchedulePeriod() > 0) {
                        deliverSMMessage.setParamScheduleDeliveryTime(CommonUtils.relativeTimeStringFromMinutes(paramatersDescriptor.getMessageSchedulePeriod()));
                    }
                }

                if (waitTimeout > 0) {
                    Object waitObject = new Object();
                    deliverSMMessage.setWaitObject(waitObject);
                    try {
                        synchronized (waitObject) {
                            retVal = ioReactor.sendDeliverSM(deliverSMMessage, returnObject);
                            waitObject.wait(waitTimeout);
                        }
                    } catch (Exception e) {
                        this.logger.error("ServerMessagingManager", "sendSms", transactionId, ioReactor.getLabel(), " : Error : " + e.getMessage(), e);
                        throw e;
                    }
                } else {
                    retVal = ioReactor.sendDeliverSM(deliverSMMessage, returnObject);
                }
            } catch (final Exception e) {
                this.logger.error("MessagingManager", "sendSms", transID, ioReactor.getLabel(), " : Error : " + e.getMessage(), e);
                return new DeliveryResult(EDeliveryResult.DeliveryFailed,
                                          IDeliveryResult.ERROR_CAUSE_NO_CONNECTION,
                                          "System error : " + e.getMessage(),
                                          null,
                                          transactionId);
            }
            if (retVal != 0) {
                return new DeliveryResult(EDeliveryResult.DeliveredSuccesfully, -1, null, deliverSMMessage, transactionId);
            } else {
                this.logger.error("ServerMessagingManager",
                                  "sendDeliveryMessage",
                                  0,
                                  null,
                                  "ServerMessagingManager->sendDeliveryMessage-> Error : Transaction Id is NULL! ");
                return new DeliveryResult(EDeliveryResult.DeliveryFailed,
                                          DeliveryResult.ERROR_CAUSE_FATAL_ERROR,
                                          "Transaction Id is NULL",
                                          deliverSMMessage,
                                          transactionId);
            }
        } else {
            this.logger.error("ServerMessagingManager", "sendSms", 0, ioReactor.getLabel(), " Sending message is not allowed through this connection.");
            DeliveryResult deliveryResult = new DeliveryResult(EDeliveryResult.DeliveryFailed,
                                                               IDeliveryResult.ERROR_CAUSE_CONNECTION_READONLY,
                                                               "Sending message is not allowed through this connection : " + ioReactor.getLabel(),
                                                               null,
                                                               transactionId);

            return deliveryResult;
        }
    }


}
