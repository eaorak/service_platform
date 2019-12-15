package com.adenon.sp.channel.smsserver.provider;

import java.security.InvalidParameterException;

import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliverSMMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliveryMessage;
import com.adenon.sp.channel.smsserver.api.message.operation.ISmsServerSendOperations;
import com.adenon.sp.channels.api.AbstractProvider;
import com.adenon.sp.channels.api.IApiMessaging;
import com.adenon.sp.kernel.dialog.IDialogHandler;



public class SmsServerMessageProvider extends AbstractProvider implements ISmsServerSendOperations {


    public SmsServerMessageProvider(IApiMessaging apiMessaging) {
        super(apiMessaging);
    }

    @Override
    public void sendDelivery(SmsServerDeliveryMessage smsServerDeliveryMessage,
                             IDialogHandler handler) throws Exception {
        if (smsServerDeliveryMessage == null) {
            throw new NullPointerException("message can not be null!");
        }
        if ((smsServerDeliveryMessage.getUsername() == null) || (smsServerDeliveryMessage.getConnectionName() == null)) {
            throw new InvalidParameterException("username and connection name must be provided!");
        }
        if (smsServerDeliveryMessage.getDeliveryStatus() == null) {
            throw new InvalidParameterException("you must provide delivery status for delivery message");
        }
        this.messaging.sendStatefulMessage(smsServerDeliveryMessage, handler);

    }

    @Override
    public void sendDeliverSM(SmsServerDeliverSMMessage smsServerDeliverSMMessage,
                              IDialogHandler handler) throws Exception {
        if (smsServerDeliverSMMessage == null) {
            throw new NullPointerException("message can not be null!");
        }
        if ((smsServerDeliverSMMessage.getUsername() == null) || (smsServerDeliverSMMessage.getConnectionName() == null)) {
            throw new InvalidParameterException("username and connection name must be present!");
        }
        this.messaging.sendStatefulMessage(smsServerDeliverSMMessage, handler);
    }
}
