package com.adenon.api.smpp.sample;

import com.adenon.api.smpp.SmppMessagingApi;
import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.TextMessageDescriptor;


public class ApiSampleSMSSendThread extends Thread {

    private final SmppMessagingApi smppMessagingApi;
    private final int              count;

    public ApiSampleSMSSendThread(SmppMessagingApi smppMessagingApi,
                                  int count) {
        this.smppMessagingApi = smppMessagingApi;
        this.count = count;

    }


    @Override
    public void run() {
        for (int i = 0; i < this.count; i++) {
            try {
                this.smppMessagingApi.sendSms(null,
                                              TextMessageDescriptor.getAnsiMessageDescriptor("hello"),
                                              -1,
                                              AddressDescriptor.getDefaultInternationalAdressDescriptor("905434567890"),
                                              AddressDescriptor.getDefaultInternationalAdressDescriptor("905434567890"),
                                              new AdditionalParamatersDescriptor().setRequestDelivery(true),
                                              null);
            } catch (Exception e) {
            }

        }
    }

}
