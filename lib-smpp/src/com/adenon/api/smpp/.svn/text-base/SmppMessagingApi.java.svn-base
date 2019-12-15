package com.adenon.api.smpp;

import com.adenon.api.smpp.core.SmppApiDelegator;
import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInfo;
import com.adenon.api.smpp.sdk.IMessage;
import com.adenon.api.smpp.sdk.ISMSSendResult;


public class SmppMessagingApi {

    private final SmppApiDelegator smppApiDelegator;

    public SmppMessagingApi(SmppApiDelegator smppApiDelegator) {
        this.smppApiDelegator = smppApiDelegator;
    }

    public ISMSSendResult sendSms(ConnectionInfo connectionDescriptor,
                                  IMessage messageDescriptor,
                                  long transactionId,
                                  AddressDescriptor destinationAddress,
                                  AddressDescriptor originatingAddress,
                                  AdditionalParamatersDescriptor paramatersDescriptor,
                                  Object attachedObject) {
        return this.smppApiDelegator.getMessagingManager().sendSms(connectionDescriptor,
                                                                   messageDescriptor,
                                                                   transactionId,
                                                                   destinationAddress,
                                                                   originatingAddress,
                                                                   paramatersDescriptor,
                                                                   attachedObject,
                                                                   -1);

    }

    public ISMSSendResult syncSendSms(ConnectionInfo connectionDescriptor,
                                      IMessage messageDescriptor,
                                      long transactionId,
                                      AddressDescriptor destinationAddress,
                                      AddressDescriptor originatingAddress,
                                      AdditionalParamatersDescriptor paramatersDescriptor,
                                      Object attachedObject,
                                      long waitTimeout) {
        return this.smppApiDelegator.getMessagingManager().syncSendSms(connectionDescriptor,
                                                                       messageDescriptor,
                                                                       transactionId,
                                                                       destinationAddress,
                                                                       originatingAddress,
                                                                       paramatersDescriptor,
                                                                       attachedObject,
                                                                       waitTimeout);

    }

}
