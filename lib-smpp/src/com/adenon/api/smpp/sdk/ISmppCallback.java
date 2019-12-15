package com.adenon.api.smpp.sdk;

import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.message.SubmitSMMessage;

public interface ISmppCallback {

    public void connected(ConnectionInformation hostInformation);

    public void disconnected(ConnectionInformation hostInformation);

    public void binded(ConnectionInformation hostInformation);

    public boolean deliverSMReceived(ConnectionInformation hostInformation,
                                     DeliverSMMessage deliverSM);

    public boolean submitSMReceived(ConnectionInformation hostInformation,
                                    SubmitSMMessage submitSM);

    public boolean deliveryReceived(ConnectionInformation hostInformation,
                                    DeliverSMMessage deliverSM,
                                    int messageStatus);

    public boolean submitResult(ConnectionInformation hostInformation,
                                SubmitSMMessage submitSM,
                                Object returnObject);

    public boolean cancelResult(ConnectionInformation hostInformation,
                                int sequenceNumber,
                                int errorCause,
                                String msgId);

    public void alarm(ConnectionInformation hostInformation,
                      AlarmCode alarmCode,
                      String alarmDescription);

}
