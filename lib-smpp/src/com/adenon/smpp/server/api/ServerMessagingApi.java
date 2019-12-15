package com.adenon.smpp.server.api;

import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.EDeliveryStatus;
import com.adenon.api.smpp.sdk.IMessage;
import com.adenon.smpp.server.core.IDeliveryResult;
import com.adenon.smpp.server.core.ServerApiDelegator;


public class ServerMessagingApi {

    private final ServerApiDelegator serverApiDelegator;

    public ServerMessagingApi(ServerApiDelegator serverApiDelegator) {
        this.serverApiDelegator = serverApiDelegator;
    }

    public IDeliveryResult sendDelivery(String connectionName,
                                        String messageIdentifier,
                                        EDeliveryStatus deliveryStatus,
                                        long transactionId,
                                        AddressDescriptor destinationAddress,
                                        AddressDescriptor originatingAddress,
                                        AdditionalParamatersDescriptor paramatersDescriptor,
                                        Object attachedObject) {
        return this.serverApiDelegator.getServerMessagingManager().sendDelivery(connectionName,
                                                                                messageIdentifier,
                                                                                deliveryStatus,
                                                                                transactionId,
                                                                                destinationAddress,
                                                                                originatingAddress,
                                                                                paramatersDescriptor,
                                                                                attachedObject);

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
        return this.serverApiDelegator.getServerMessagingManager().syncSendDelivery(connectionName,
                                                                                    messageIdentifier,
                                                                                    deliveryStatus,
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
        return this.serverApiDelegator.getServerMessagingManager().sendDeliverSM(connectionName,
                                                                                 messageDescriptor,
                                                                                 transactionId,
                                                                                 destinationAddress,
                                                                                 originatingAddress,
                                                                                 paramatersDescriptor,
                                                                                 attachedObject);

    }

    public IDeliveryResult syncSendDeliverSM(String connectionName,
                                             IMessage messageDescriptor,
                                             long transactionId,
                                             AddressDescriptor destinationAddress,
                                             AddressDescriptor originatingAddress,
                                             AdditionalParamatersDescriptor paramatersDescriptor,
                                             Object attachedObject,
                                             long waitTimeout) {
        return this.serverApiDelegator.getServerMessagingManager().syncSendDeliverSM(connectionName,
                                                                                     messageDescriptor,
                                                                                     transactionId,
                                                                                     destinationAddress,
                                                                                     originatingAddress,
                                                                                     paramatersDescriptor,
                                                                                     attachedObject,
                                                                                     waitTimeout);

    }
}
