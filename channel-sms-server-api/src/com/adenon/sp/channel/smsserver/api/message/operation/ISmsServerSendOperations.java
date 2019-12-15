package com.adenon.sp.channel.smsserver.api.message.operation;

import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliverSMMessage;
import com.adenon.sp.channel.smsserver.api.message.SmsServerDeliveryMessage;
import com.adenon.sp.kernel.dialog.IDialogHandler;



public interface ISmsServerSendOperations {

    // BEGIN
    public void sendDelivery(SmsServerDeliveryMessage smsServerDeliveryMessage,
                             IDialogHandler handler) throws Exception;

    // BEGIN
    public void sendDeliverSM(SmsServerDeliverSMMessage smsServerDeliverSMMessage,
                              IDialogHandler handler) throws Exception;

}
