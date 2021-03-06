package com.adenon.channel.sms.messaging;

import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInfo;
import com.adenon.api.smpp.sdk.ESendResult;
import com.adenon.api.smpp.sdk.ISMSSendResult;
import com.adenon.api.smpp.sdk.TextMessageDescriptor;
import com.adenon.channel.sms.SmsActivator;
import com.adenon.channel.sms.api.message.ESendSMSResult;
import com.adenon.channel.sms.api.message.ESendType;
import com.adenon.channel.sms.api.message.SmsAckMessage;
import com.adenon.channel.sms.api.message.SmsMessageTypes;
import com.adenon.channel.sms.api.message.SmsSendMessage;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.event.Event;


public class SmsChannelProtocol implements IChannelProtocol {

    private final SmsActivator      smsActivator;
    private final IChannelMessaging channelMessaging;


    public SmsChannelProtocol(SmsActivator smsActivator,
                              IChannelMessaging messaging) {
        this.smsActivator = smsActivator;
        this.channelMessaging = messaging;
    }

    @Override
    public void receiveBegin(final Event event) {
        SmsMessageTypes messageTypes = (SmsMessageTypes) event.getMessage().getId();
        switch (messageTypes) {
            case SMS_MESSAGE_SEND:
                SmsSendMessage sendSmsMessage = (SmsSendMessage) event.getMessage();
                IDialog dialog = event.getDialog();
                ConnectionInfo connectionInfo = new ConnectionInfo().setConnectionGroupName(sendSmsMessage.getConnectionGroupName())
                                                                    .setConnectionName(sendSmsMessage.getConnectionName());

                TextMessageDescriptor messageDescriptor = new TextMessageDescriptor().setDataCoding(sendSmsMessage.getDataCoding())
                                                                                     .setMessage(sendSmsMessage.getMessage());


                AddressDescriptor destinationAddress = new AddressDescriptor().setNumber(sendSmsMessage.getDestinationNumber());
                // .setNpi(sendSmsMessage.getDestinationNpi())
                // .setTon(sendSmsMessage.getDestinationTon());

                AddressDescriptor originatingAddress = new AddressDescriptor().setNumber(sendSmsMessage.getSourceNumber());
                // .setNpi(sendSmsMessage.getSourceNpi())
                // .setTon(sendSmsMessage.getSourceTon());

                AdditionalParamatersDescriptor additionalParamatersDescriptor = new AdditionalParamatersDescriptor().setRequestDelivery(sendSmsMessage.isRequestDeliver());

                long transId = this.smsActivator.getLongSequenceGenerator().getNextLongSequenceNum();
                if (sendSmsMessage.getSendType() == ESendType.ASYNC) {
                    this.smsActivator.getSmppApi()
                                     .getSmppMessagingApi()
                                     .sendSms(connectionInfo,
                                              messageDescriptor,
                                              transId,
                                              destinationAddress,
                                              originatingAddress,
                                              additionalParamatersDescriptor,
                                              dialog);
                } else {
                    ISMSSendResult syncSendSms = this.smsActivator.getSmppApi()
                                                                  .getSmppMessagingApi()
                                                                  .syncSendSms(connectionInfo,
                                                                               messageDescriptor,
                                                                               transId,
                                                                               destinationAddress,
                                                                               originatingAddress,
                                                                               additionalParamatersDescriptor,
                                                                               dialog,
                                                                               30000);
                    SmsAckMessage ackMessage = new SmsAckMessage();
                    if (syncSendSms.getSendResult() == ESendResult.SUCCESS) {
                        ackMessage.setSendResult(ESendSMSResult.SUCCESS);
                        ackMessage.setMsgId(syncSendSms.getMessage().getMessageIdentifier());
                        ackMessage.setTransactionId(transId);
                        ackMessage.setMessageCount(syncSendSms.getMessage().getMsgPartCount());
                    } else {
                        ESendSMSResult sendSMSResult;
                        if (syncSendSms.getSendResult() == ESendResult.RETRY) {
                            sendSMSResult = ESendSMSResult.RETRY;
                        } else {
                            sendSMSResult = ESendSMSResult.FAILED;
                        }
                        ackMessage.setSendResult(sendSMSResult);
                        ackMessage.setMsgId(null);
                        ackMessage.setTransactionId(transId);
                        ackMessage.setErrorDescription(syncSendSms.getErrorDescription());
                        ackMessage.setErrorCause(syncSendSms.getErrorCause());
                    }
                    this.channelMessaging.end(dialog, ackMessage);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void receiveContinue(final Event event) {

    }

    @Override
    public void receiveEnd(final Event event) {

    }

    @Override
    public void receiveFail(final Event event) {

    }

}
