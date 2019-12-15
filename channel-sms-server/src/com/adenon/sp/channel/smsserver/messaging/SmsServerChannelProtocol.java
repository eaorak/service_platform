package com.adenon.sp.channel.smsserver.messaging;

import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.TextMessageDescriptor;
import com.adenon.smpp.server.api.ServerMessagingApi;
import com.adenon.smpp.server.core.EDeliveryResult;
import com.adenon.smpp.server.core.IDeliveryResult;
import com.adenon.sp.channel.smsserver.SmsServerActivator;
import com.adenon.sp.channel.smsserver.api.error.SmsServerError;
import com.adenon.sp.channel.smsserver.api.message.EDeliverySendResult;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliverSMMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliverSendResultMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliveryMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerMessageTypes;
import com.adenon.sp.channel.smsserver.beans.UserBean;
import com.adenon.sp.channel.smsserver.beans.UsersBean;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.error.ErrorActions;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.IMessage;



public class SmsServerChannelProtocol implements IChannelProtocol {


    private final SmsServerActivator smsServerActivator;
    private final IChannelMessaging  channelMessaging;
    private ServerMessagingApi       serverMessagingApi;

    public SmsServerChannelProtocol(SmsServerActivator smsServerActivator,
                                    IChannelMessaging channelMessaging) {
        this.smsServerActivator = smsServerActivator;
        this.channelMessaging = channelMessaging;
    }

    @Override
    public void receiveBegin(Event event) {
        IMessage message = event.getMessage();
        SmsServerMessageTypes id = (SmsServerMessageTypes) message.getId();
        IDeliveryResult sendDeliveryResult = null;
        IMessage sentMessage = null;
        UsersBean usersBean = this.smsServerActivator.getUsersBean();
        String username = null;
        UserBean userBean = null;
        switch (id) {
            case SERVER_MESSAGE_DELIVERY:
                SmsServerDeliveryMessage deliveryMessage = (SmsServerDeliveryMessage) message;
                sendDeliveryResult = this.getServerMessagingApi().sendDelivery(deliveryMessage.getConnectionName(),
                                                                               deliveryMessage.getMessageId(),
                                                                               deliveryMessage.getDeliveryStatus(),
                                                                               deliveryMessage.getTransactionId(),
                                                                               AddressDescriptor.getDefaultAdressDescriptor(deliveryMessage.getDestination()),
                                                                               AddressDescriptor.getDefaultAdressDescriptor(deliveryMessage.getOriginating()),
                                                                               new AdditionalParamatersDescriptor().setRequestDelivery(true),
                                                                               event);
                username = deliveryMessage.getUsername();
                sentMessage = deliveryMessage;
                break;

            case SERVER_MESSAGE_DELIVERSM:
                SmsServerDeliverSMMessage deliverSMMessage = (SmsServerDeliverSMMessage) message;
                sendDeliveryResult = this.getServerMessagingApi()
                                         .sendDeliverSM(deliverSMMessage.getConnectionName(),
                                                        TextMessageDescriptor.getAnsiMessageDescriptor(deliverSMMessage.getMessage())
                                                                             .setDataCoding(deliverSMMessage.getDataCoding()),
                                                        deliverSMMessage.getTransactionId(),
                                                        AddressDescriptor.getDefaultAdressDescriptor(deliverSMMessage.getDestination()),
                                                        AddressDescriptor.getDefaultAdressDescriptor(deliverSMMessage.getOriginating()),
                                                        new AdditionalParamatersDescriptor().setRequestDelivery(true),
                                                        event);
                username = deliverSMMessage.getUsername();
                sentMessage = deliverSMMessage;
                break;

            default:
                break;
        }
        if (sendDeliveryResult != null) {
            EDeliverySendResult deliverySendResult = EDeliverySendResult.SUCCESS;
            EDeliveryResult deliveryResult = sendDeliveryResult.getDeliveryResult();
            if (username != null) {
                userBean = usersBean.getUser(username);
            }
            if (deliveryResult != EDeliveryResult.DeliveredSuccesfully) {
                SmsServerError serverError = null;
                switch (deliveryResult) {
                    case DeliveryFailed:
                        serverError = new SmsServerError("Delivery is not successful",
                                                         ErrorActions.ABORT,
                                                         sendDeliveryResult.getErrorDescription(),
                                                         sendDeliveryResult.getErrorCause());
                        deliverySendResult = EDeliverySendResult.ERROR;

                        if (userBean != null) {
                            switch (id) {
                                case SERVER_MESSAGE_DELIVERY:
                                    userBean.increaseCounterFailedDelivery();
                                    break;
                                case SERVER_MESSAGE_DELIVERSM:
                                    userBean.increaseCounterFailedDeliverSM();
                                    break;
                            }

                        }
                        break;

                    case RetryDelivery:
                        serverError = new SmsServerError("Delivery should be retried.",
                                                         ErrorActions.RETRY,
                                                         sendDeliveryResult.getErrorDescription(),
                                                         sendDeliveryResult.getErrorCause());
                        deliverySendResult = EDeliverySendResult.RETRY;
                        if (userBean != null) {
                            switch (id) {
                                case SERVER_MESSAGE_DELIVERY:
                                    userBean.increaseCounterRetryDelivery();
                                    break;
                                case SERVER_MESSAGE_DELIVERSM:
                                    userBean.increaseCounterRetryDeliverSM();
                                    break;
                            }

                        }

                        break;

                    default:
                        serverError = new SmsServerError("Delivery is not successful",
                                                         ErrorActions.ABORT,
                                                         sendDeliveryResult.getErrorDescription(),
                                                         sendDeliveryResult.getErrorCause());
                        if (userBean != null) {
                            switch (id) {
                                case SERVER_MESSAGE_DELIVERY:
                                    userBean.increaseCounterFailedDelivery();
                                    break;
                                case SERVER_MESSAGE_DELIVERSM:
                                    userBean.increaseCounterFailedDeliverSM();
                                    break;
                            }

                        }
                        deliverySendResult = EDeliverySendResult.ERROR;
                        break;
                }
                serverError.setMessage(sentMessage);
                event.setError(serverError);
                SmsServerDeliverSendResultMessage deliverSendResultMessage = new SmsServerDeliverSendResultMessage();
                deliverSendResultMessage.setMessage(event.getMessage());
                deliverSendResultMessage.setErrorOccured(true);
                deliverSendResultMessage.setServerError(serverError);
                deliverSendResultMessage.setDeliverySendResult(deliverySendResult);
                this.channelMessaging.end(event.getDialog(), deliverSendResultMessage);
            } else {
                if (userBean != null) {
                    switch (id) {
                        case SERVER_MESSAGE_DELIVERY:
                            userBean.increaseCounterSuccessfullDelivery();
                            break;
                        case SERVER_MESSAGE_DELIVERSM:
                            userBean.increaseCounterSuccessfullDeliverSM();
                            break;
                    }

                }
                SmsServerDeliverSendResultMessage deliverSendResultMessage = new SmsServerDeliverSendResultMessage();
                deliverSendResultMessage.setMessage(event.getMessage());
                deliverSendResultMessage.setErrorOccured(false);
                deliverSendResultMessage.setDeliverySendResult(EDeliverySendResult.SUCCESS);
                this.channelMessaging.end(event.getDialog(), deliverSendResultMessage);
            }

        }
    }

    @Override
    public void receiveContinue(Event event) {

    }

    @Override
    public void receiveEnd(Event event) {

    }

    @Override
    public void receiveFail(Event event) {

    }

    public ServerMessagingApi getServerMessagingApi() {
        return this.serverMessagingApi;
    }

    public void setServerMessagingApi(ServerMessagingApi serverMessagingApi) {
        this.serverMessagingApi = serverMessagingApi;
    }

}
