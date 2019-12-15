package com.adenon.channel.sms.proxy;

import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.AlarmCode;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.api.smpp.sdk.ESendResult;
import com.adenon.api.smpp.sdk.ISmppCallback;
import com.adenon.channel.sms.api.message.ESendSMSResult;
import com.adenon.channel.sms.api.message.SmsAckMessage;
import com.adenon.channel.sms.api.message.SmsReceiveMessage;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.dialog.Dialog;
import com.adenon.sp.kernel.dialog.DialogType;



public class SmsApiProxy implements ISmppCallback {


    private final IChannelMessaging messaging;

    public SmsApiProxy(IChannelMessaging messaging) {
        this.messaging = messaging;
    }

    @Override
    public void connected(ConnectionInformation hostInformation) {

    }

    @Override
    public void disconnected(ConnectionInformation hostInformation) {

    }

    @Override
    public void binded(ConnectionInformation hostInformation) {

    }

    @Override
    public boolean deliverSMReceived(ConnectionInformation hostInformation,
                                     DeliverSMMessage deliverSM) {

        if (deliverSM == null) {
            return false;
        }

        SmsReceiveMessage smsReceiveMessage = new SmsReceiveMessage();

        AddressDescriptor sourceAddress = deliverSM.getSourceAddress();
        if (sourceAddress == null) {
            return false;
        }
        String origNumber = sourceAddress.getNumber();
        smsReceiveMessage.setSource(origNumber);

        AddressDescriptor destinationAddress = deliverSM.getSourceAddress();
        if (destinationAddress == null) {
            return false;
        }
        String destNumber = destinationAddress.getNumber();
        smsReceiveMessage.setSource(destNumber);

        smsReceiveMessage.setMessage(deliverSM.getParamShortMessage());
        smsReceiveMessage.setDataCoding(deliverSM.getParamDataCoding());

        this.messaging.begin(smsReceiveMessage, DialogType.STATELESS, 10000);
        return true;
    }

    @Override
    public boolean submitSMReceived(ConnectionInformation hostInformation,
                                    SubmitSMMessage submitSM) {
        return false;
    }

    @Override
    public boolean deliveryReceived(ConnectionInformation hostInformation,
                                    DeliverSMMessage deliverSM,
                                    int messageStatus) {
        return false;
    }


    @Override
    public boolean cancelResult(ConnectionInformation hostInformation,
                                int sequenceNumber,
                                int errorCause,
                                String msgId) {
        return false;
    }

    @Override
    public void alarm(ConnectionInformation hostInformation,
                      AlarmCode alarmCode,
                      String alarmDescription) {

    }

    @Override
    public boolean submitResult(ConnectionInformation hostInformation,
                                SubmitSMMessage submitSM,
                                Object returnObject) {
        SmsAckMessage ackMessage = new SmsAckMessage();
        if (submitSM.getSendResult() == ESendResult.SUCCESS) {
            ackMessage.setSendResult(ESendSMSResult.SUCCESS);
            ackMessage.setMsgId(submitSM.getMessageIdentifier());
            ackMessage.setTransactionId(submitSM.getTransID());
            ackMessage.setMessageCount(submitSM.getMsgPartCount());
        } else {
            ESendSMSResult sendSMSResult;
            if (submitSM.getSendResult() == ESendResult.RETRY) {
                sendSMSResult = ESendSMSResult.RETRY;
            } else {
                sendSMSResult = ESendSMSResult.FAILED;
            }
            ackMessage.setSendResult(sendSMSResult);
            ackMessage.setMsgId(null);
            ackMessage.setTransactionId(submitSM.getTransID());
            ackMessage.setErrorDescription("Server returned error : " + submitSM.getServerErrorCode());
            ackMessage.setErrorCause(submitSM.getServerErrorCode());
        }
        Dialog dialog = (Dialog) returnObject;
        this.messaging.end(dialog, ackMessage);

        return true;
    }

}
