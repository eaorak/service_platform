package com.adenon.channel.sms.messaging;

import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInfo;
import com.adenon.api.smpp.sdk.TextMessageDescriptor;
import com.adenon.channel.sms.SmsActivator;
import com.adenon.channel.sms.api.message.SmsMessageTypes;
import com.adenon.channel.sms.api.message.SmsSendMessage;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.event.Event;


public class SmsMessageProcessor implements IChannelProtocol {

    private final SmsActivator smsActivator;

    public SmsMessageProcessor(SmsActivator smsActivator) {
        this.smsActivator = smsActivator;
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

                this.smsActivator.getSmppApi().getSmppMessagingApi().sendSms(connectionInfo, messageDescriptor,
                // this.smsActivator.getIntegerSequenceGenerator().getNextIntegerSequenceNum(),
                                                                             1,
                                                                             destinationAddress,
                                                                             originatingAddress,
                                                                             additionalParamatersDescriptor,
                                                                             dialog);
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
