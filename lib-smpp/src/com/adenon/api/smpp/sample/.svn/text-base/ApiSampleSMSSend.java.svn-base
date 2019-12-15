package com.adenon.api.smpp.sample;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Level;

import com.adenon.api.smpp.SmppApi;
import com.adenon.api.smpp.SmppApiEngine;
import com.adenon.api.smpp.SmppConnectionApi;
import com.adenon.api.smpp.SmppMessagingApi;
import com.adenon.api.smpp.common.State;
import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.api.smpp.sdk.AdditionalParamatersDescriptor;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.AlarmCode;
import com.adenon.api.smpp.sdk.ApiProperties;
import com.adenon.api.smpp.sdk.ConnectionDescriptor;
import com.adenon.api.smpp.sdk.ConnectionGroupDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.api.smpp.sdk.ISMSSendResult;
import com.adenon.api.smpp.sdk.ISmppCallback;
import com.adenon.api.smpp.sdk.LogDescriptor;
import com.adenon.api.smpp.sdk.SmppConnectionType;
import com.adenon.api.smpp.sdk.WapPushBookmarkMessageDescriptor;

public class ApiSampleSMSSend implements ISmppCallback {


    private final SmppMessagingApi smppMessagingApi;
    private AtomicInteger          deliveryCounter = new AtomicInteger(0);

    public ApiSampleSMSSend(SmppMessagingApi smppMessagingApi) {
        this.smppMessagingApi = smppMessagingApi;
    }

    @Override
    public void alarm(ConnectionInformation hostInformation,
                      AlarmCode alarmCode,
                      String alarmDescription) {
    }

    @Override
    public void binded(ConnectionInformation hostInformation) {
        // for (int i = 0; i < 5; i++) {
        // final ApiSampleSMSSendThread sendThread = new ApiSampleSMSSendThread(this.smppMessagingApi, 2);
        // sendThread.start();
        // }
    }

    @Override
    public boolean cancelResult(ConnectionInformation hostInformation,
                                int _sequence,
                                int _errorCode,
                                String msg_id) {
        return false;
    }

    @Override
    public void connected(ConnectionInformation hostInformation) {
    }

    @Override
    public boolean deliverSMReceived(ConnectionInformation hostInformation,
                                     DeliverSMMessage deliverSM) {
        System.err.println("I got *DELIVER_SM* : " + deliverSM.getParamShortMessage());
        return true;
    }

    @Override
    public boolean deliveryReceived(ConnectionInformation hostInformation,
                                    DeliverSMMessage deliverSM,
                                    int messageStatus) {
        int incrementAndGet = this.deliveryCounter.incrementAndGet();
        System.err.println("I got *DELIVERY* : " + deliverSM.getMessageIdentifier() + " count : " + incrementAndGet);
        return true;
    }

    @Override
    public void disconnected(ConnectionInformation hostInformation) {
    }

    @Override
    public boolean submitResult(ConnectionInformation hostInformation,
                                SubmitSMMessage submitSM,
                                Object returnObject) {
        System.err.println("I got *ACK* : " + submitSM.getMessageIdentifier());
        return true;
    }

    @Override
    public boolean submitSMReceived(ConnectionInformation hostInformation,
                                    SubmitSMMessage submitSM) {
        return true;
    }

    public static void main(final String[] args) {
        final SmppApiEngine apiEngine = SmppApiEngine.getSmppApiEngine("myEngine", 500);
        final SmppApi smppApi = apiEngine.getSmppApi(LogDescriptor.getDefaultLogDescriptor().setWriteConsole(true).setLevel(Level.DEBUG),
                                                     new ApiProperties().setThreadCount(100));
        final SmppConnectionApi smppConnectionApi = smppApi.getSmppConnectionApi();
        final SmppMessagingApi smppMessagingApi = smppApi.getSmppMessagingApi();
        final ApiSampleSMSSend apiSampleSMSSend = new ApiSampleSMSSend(smppMessagingApi);
        final ConnectionGroupDescriptor connectionGroup = smppConnectionApi.generateConnectionGroup("osman");
        final ConnectionDescriptor connectionDescriptor = connectionGroup.generateConnection("con1")
                                                                         .addIp("172.30.100.26")
                                                                         .addIp("172.30.100.26 ")
                                                                         .setPort(5101)
                                                                         .setUsername("osman")
                                                                         .setPassword("osman")
                                                                         .setTraceON(true)
                                                                         .setMaxThreadCount(10)
                                                                         .setCallbackInterface(apiSampleSMSSend)
                                                                         .setConnectionType(SmppConnectionType.BOTH);

        connectionGroup.addConnection(connectionDescriptor);

        try {
            smppConnectionApi.createConnectionGroup(connectionGroup);
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        final State state = smppConnectionApi.getState();
        if (state.waitIdle()) {

            // ISMSSendResult sendResult2 = smppMessagingApi.syncSendSms(null,
            // TextMessageDescriptor.getAnsiMessageDescriptor("Hello my name is Ongun adhjhsad asdhasdjkhasd asdhajshdajsd asjdhajshdjasd asjhdajshdkasd  a sd sad  asd sad  asd a sd asd a d asd ads")
            // .setDataCoding(8),
            // -1,
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // AddressDescriptor.getDefaultNationalAdressDescriptor("9090"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);

            // sendResult2 = smppMessagingApi.syncSendSms(null,
            // TextMessageDescriptor.getAnsiMessageDescriptor("Hello my name is Ongun").setDataCoding(0),
            // -1,
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);
            //
            // sendResult2 = smppMessagingApi.syncSendSms(null,
            // TextMessageDescriptor.getAnsiMessageDescriptor("Hello my name is Ongun").setDataCoding(0),
            // -1,
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);
            //
            // ISMSSendResult sendResult = smppMessagingApi.syncSendSms(null,
            // WapPushSIMessageDescriptor.getDefaultWapPushSI("Hello this  hsdhashg asdgajhsgd hasgd hasgdsa ewruewr we rew rew rewrewre dfjsdhf dshfsdhf shdf shd fh sdf shd fshd fhfds h:",
            // "http://www.google.com/albeni/gotur/adhd/dsd/services/help/get/melp/zelp.htm"),
            // -1,
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // AddressDescriptor.getDefaultNationalAdressDescriptor("9090"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);
            // ISMSSendResult sendResult2 = smppMessagingApi.syncSendSms(null,
            // WapPushSLMessageDescriptor.getDefaultWapPushSL("https://www.google.com.tr/help"),
            // -1,
            // AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
            // AddressDescriptor.getDefaultNationalAdressDescriptor("9090"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);

            ISMSSendResult sendResult = smppMessagingApi.syncSendSms(null,
                                                                     WapPushBookmarkMessageDescriptor.getDefaultWapPushBookmarkMessageDescriptor("Bana ne olm",
                                                                                                                                                 "http://deneme.com"),
                                                                     -1,
                                                                     AddressDescriptor.getDefaultInternationalAdressDescriptor("905435022313"),
                                                                     AddressDescriptor.getDefaultNationalAdressDescriptor("9090"),
                                                                     new AdditionalParamatersDescriptor().setRequestDelivery(true),
                                                                     null,
                                                                     10000);

            if (sendResult != null) {
                System.err.println("Message sent : "
                                   + sendResult.getMessage().getMessageIdentifier()
                                   + " Message Count : "
                                   + sendResult.getMessage().getMsgPartCount());
            }

            // long msisdn = 905323210000L;
            // for (int i = 0; i < 2000; i++) {
            // ISMSSendResult sendResult = smppMessagingApi.syncSendSms(null,
            // WapPushSIMessageDescriptor.getDefaultWapPushSI("Hello this is me you looking for you baby jane dont leave me tonight and dont ",
            // "http://www.adenon.com/albeni/gotur"),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);
            //
            // sendResult = smppMessagingApi.syncSendSms(null,
            // WapPushBookmarkMessageDescriptor.getDefaultWapPushBookmarkMessageDescriptor("Bana ne olm",
            // "http://deneme.com"),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);
            //
            // sendResult = smppMessagingApi.syncSendSms(null,
            // WapPushSLMessageDescriptor.getDefaultWapPushSL("https://www.adenon.com.tr/help"),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);

            // sendResult = smppMessagingApi.syncSendSms(null,
            // TextMessageDescriptor.getAnsiMessageDescriptor("Hello my name is Orhan and I am writing this program for open source community. I would like to introduce my program to whole who likes me . Hello again it is hard to write this yaaaa.")
            // .setDataCoding(8),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);
            // msisdn++;
            // ISMSSendResult sendResult = smppMessagingApi.syncSendSms(null,
            // TextMessageDescriptor.getAnsiMessageDescriptor("Hello").setDataCoding(8),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor(Long.toString(msisdn)),
            // AddressDescriptor.getDefaultAdressDescriptor("9090"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null,
            // 10000);


            // if (sendResult != null) {
            // System.err.println("Message sent : "
            // + sendResult.getMessage().getMessageIdentifier()
            // + " Message Count : "
            // + sendResult.getMessage().getMsgPartCount()
            // + " count : "
            // + i);
            // }

            // sendResult = smppMessagingApi.sendSms(null,
            // TextMessageDescriptor.getAnsiMessageDescriptor("Hello my name is Orhan and I am writing this program for open source community. I would like to introduce my program to whole who likes me . Hello again it is hard to write this yaaaa."),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null);
            // if (sendResult != null) {
            // System.err.println("Message sent : " + sendResult.getMessage().getMessageIdentifier() + " count : " + i);
            // }
            // smppMessagingApi.sendSms(null,
            // WapPushSIMessageDescriptor.getDefaultWapPushSI("Bu adenon wap push icerigi", "http://www.adenon.com"),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null);
            //
            // smppMessagingApi.sendSms(null,
            // WapPushSLMessageDescriptor.getDefaultWapPushSL("http://www.adenon.com"),
            // -1,
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // AddressDescriptor.getDefaultAdressDescriptor("905434567890"),
            // new AdditionalParamatersDescriptor().setRequestDelivery(true),
            // null);
            // }
        }

        try {
            Thread.sleep(1000000000L);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
