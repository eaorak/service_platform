package com.adenon.sp.channel.smsserver.proxy;

import org.apache.log4j.Logger;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.message.BindRequestMessage;
import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.library.common.utils.ETime;
import com.adenon.library.common.utils.StringUtils;
import com.adenon.smpp.server.callback.response.BindResponse;
import com.adenon.smpp.server.callback.response.EBindResult;
import com.adenon.smpp.server.callback.response.ESubmitResult;
import com.adenon.smpp.server.callback.response.SubmitResponse;
import com.adenon.smpp.server.core.IServerCallback;
import com.adenon.sp.channel.smsserver.SmsServerActivator;
import com.adenon.sp.channel.smsserver.api.message.EDeliverySendResult;
import com.adenon.sp.channel.smsserver.api.message.SmsServerBindInfoMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliverSendResultMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDisconnectMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerSubmitMessage;
import com.adenon.sp.channel.smsserver.beans.UserBean;
import com.adenon.sp.channel.smsserver.beans.UsersBean;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.event.Event;



public class SmsServerProxy implements IServerCallback {

    private static final Logger      logger = Logger.getLogger(SmsServerProxy.class);
    private final SmsServerActivator smsServerActivator;
    private final IChannelMessaging  messaging;

    public SmsServerProxy(SmsServerActivator smsServerActivator,
                          IChannelMessaging messaging) {
        this.smsServerActivator = smsServerActivator;
        this.messaging = messaging;
    }

    @Override
    public void disconnected(ConnectionInformation connectionInformation) {
        SmsServerDisconnectMessage smsServerDisconnectMessage = new SmsServerDisconnectMessage();
        smsServerDisconnectMessage.setConnectionName(connectionInformation.getConnectionName());
        smsServerDisconnectMessage.setUsername(connectionInformation.getUserName());
        UsersBean usersBean = this.smsServerActivator.getUsersBean();
        UserBean userBean = usersBean.getUser(connectionInformation.getUserName());
        userBean.setConnected(false);
        this.messaging.begin(smsServerDisconnectMessage, DialogType.STATELESS, 10000);

    }

    @Override
    public void deliveryResult(ConnectionInformation connectionInformation,
                               DeliverSMMessage deliverSM,
                               Object attachedObject) {
        SmsServerDeliverSendResultMessage deliverSendResult = new SmsServerDeliverSendResultMessage();
        deliverSendResult.setTransactionId(deliverSM.getTransactionId());
        EDeliverySendResult deliverySendResult = null;
        if (deliverSM.getSendResult() != null) {
            deliverySendResult = EDeliverySendResult.getDeliverySendResult(deliverSM.getSendResult().getValue());
        }
        deliverSendResult.setDeliverySendResult(deliverySendResult);
        Event event = (Event) attachedObject;
        if (event != null) {
            deliverSendResult.setMessage(event.getMessage());
            UsersBean usersBean = this.smsServerActivator.getUsersBean();
            UserBean userBean = usersBean.getUser(connectionInformation.getUserName());
            switch (deliverySendResult) {
                case ERROR:
                case FATAL_ERROR:
                    userBean.increaseDeliveryFailed();
                    break;
                case RETRY:
                    userBean.increaseDeliveryRetry();
                    break;
                case SUCCESS:
                    userBean.increaseDeliverySuccess();
                    break;

                default:
                    break;
            }
            this.messaging.end(event.getDialog(), deliverSendResult);
        }

    }

    @Override
    public BindResponse bindReceived(BindRequestMessage bindRequestMessage) {
        String username = bindRequestMessage.getUsername();
        String password = bindRequestMessage.getPassword();
        UsersBean usersBean = this.smsServerActivator.getUsersBean();
        UserBean userBean = usersBean.getUser(username);
        if ((userBean == null) || !userBean.accept(username, password)) {
            BindResponse bindResponse = null;
            try {
                bindResponse = new BindResponse(EBindResult.InvalidLogin, "");
            } catch (Exception e) {
                logger.error("SmsServerProxy->bindReceived-> Error : " + e.getMessage(), e);
            }
            return bindResponse;
        }
        String connectionNameStr = username + "Con" + this.smsServerActivator.getLongSequenceGenerator().getNextLongSequenceNum();

        userBean.setConnected(true);
        SmsServerBindInfoMessage bindInfoMessage = new SmsServerBindInfoMessage();
        bindInfoMessage.setUsername(username);
        bindInfoMessage.setConnectionName(connectionNameStr);
        bindInfoMessage.setIp(bindRequestMessage.getIp());
        this.messaging.begin(bindInfoMessage, DialogType.STATELESS, 10000);

        BindResponse bindResponse = null;
        try {
            bindResponse = new BindResponse(EBindResult.BindSuccess, connectionNameStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bindResponse;
    }

    @Override
    public SubmitResponse submitSMReceived(ConnectionInformation connectionInformation,
                                           SubmitSMMessage submitSMMessage) {
        String connectionName = connectionInformation.getConnectionName();
        String paramShortMessage = submitSMMessage.getParamShortMessage();
        AddressDescriptor destinationAddress = submitSMMessage.getDestinationAddress();
        AddressDescriptor sourceAddress = submitSMMessage.getSourceAddress();

        UsersBean usersBean = this.smsServerActivator.getUsersBean();
        UserBean userBean = usersBean.getUser(connectionInformation.getUserName());
        userBean.increaseCounterSubmitSM();

        SmsServerSubmitMessage serverSubmitMessage = new SmsServerSubmitMessage();
        serverSubmitMessage.setUserName(connectionInformation.getUserName());
        serverSubmitMessage.setConnectionName(connectionName);
        serverSubmitMessage.setMessage(paramShortMessage);
        serverSubmitMessage.setDestination(destinationAddress.getNumber());
        serverSubmitMessage.setSource(sourceAddress.getNumber());
        serverSubmitMessage.setPartIndex(submitSMMessage.getOpParamSarSegmentSequenceNum());
        serverSubmitMessage.setPartCount(submitSMMessage.getOpParamSarTotalSegments());
        serverSubmitMessage.setPartReferenceNumber(submitSMMessage.getOpParamSarMsgRefNum());

        String paramValidityPeriod = submitSMMessage.getParamValidityPeriod();

        if (StringUtils.checkStringIsEmpty(paramValidityPeriod)) {
            serverSubmitMessage.setValidityPeriod(ETime.MINUTE.getMiliseconds(60));
        } else {
            try {
                CommonUtils.milisecondsFromRelativeString(paramValidityPeriod);
            } catch (Exception e) {
                logger.error("SmsServerProxy->submitSMReceived-> Error : " + e.getMessage(), e);
                serverSubmitMessage.setValidityPeriod(0);
            }
        }

        if (submitSMMessage.getParamRegisteredDelivery() == 1) {
            serverSubmitMessage.setRequestDelivery(true);
        }
        String msgId = "Msg."
                       + sourceAddress.getNumber()
                       + "."
                       + System.currentTimeMillis()
                       + "."
                       + this.smsServerActivator.getLongSequenceGenerator().getNextLongSequenceNum();
        serverSubmitMessage.setMessageIdentifier(msgId);

        try {
            this.messaging.begin(serverSubmitMessage, DialogType.STATELESS, 10000);
            return new SubmitResponse(ESubmitResult.submitSuccess, msgId);
        } catch (Exception e) {
            logger.error("[SmsServerProxy][submitSMReceived] : Error : " + e.getMessage(), e);
            return new SubmitResponse(ESubmitResult.QueueFull, msgId);
        }
    }

}
