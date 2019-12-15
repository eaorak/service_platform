package com.adenon.smpp.server.sample;

import org.apache.log4j.Level;

import com.adenon.api.smpp.message.BindRequestMessage;
import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.api.smpp.sdk.LogDescriptor;
import com.adenon.smpp.server.api.ServerApi;
import com.adenon.smpp.server.api.ServerApiEngine;
import com.adenon.smpp.server.api.ServerConnectionApi;
import com.adenon.smpp.server.api.ServerMessagingApi;
import com.adenon.smpp.server.callback.response.BindResponse;
import com.adenon.smpp.server.callback.response.EBindResult;
import com.adenon.smpp.server.callback.response.ESubmitResult;
import com.adenon.smpp.server.callback.response.SubmitResponse;
import com.adenon.smpp.server.core.IServerCallback;
import com.adenon.smpp.server.core.ServerApiProperties;


public class ServerSample implements IServerCallback {

    @Override
    public void disconnected(ConnectionInformation connectionInformation) {

    }

    @Override
    public void deliveryResult(ConnectionInformation connectionInformation,
                               DeliverSMMessage deliverSM,
                               Object attachedObject) {

    }

    @Override
    public BindResponse bindReceived(BindRequestMessage bindRequestMessage) {
        System.out.println("BindReceived");
        BindResponse bindResponse = null;
        try {
            bindResponse = new BindResponse(EBindResult.BindSuccess, "sampleCon");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bindResponse;
    }

    @Override
    public SubmitResponse submitSMReceived(ConnectionInformation connectionInformation,
                                           SubmitSMMessage submitSMMessage) {
        System.out.println("BindReceived");
        SubmitResponse submitResponse = new SubmitResponse(ESubmitResult.submitSuccess, "msgid"
                                                                                        + System.currentTimeMillis()
                                                                                        + submitSMMessage.getSourceAddress().getNumber());
        return submitResponse;
    }

    public static void main(String[] args) {
        ServerSample serverSample = new ServerSample();
        ServerApiEngine apiEngine = ServerApiEngine.getServerApiEngine("myServer");
        ServerApi serverApi = apiEngine.getServerApi(serverSample,
                                                     LogDescriptor.getDefaultLogDescriptor().setWriteConsole(true).setLevel(Level.DEBUG),
                                                     new ServerApiProperties().setTraceOn(true));
        ServerConnectionApi serverConnectionApi = serverApi.getServerConnectionApi();
        ServerMessagingApi serverMessagingApi = serverApi.getServerMessagingApi();
        try {
            Thread.sleep(1000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
