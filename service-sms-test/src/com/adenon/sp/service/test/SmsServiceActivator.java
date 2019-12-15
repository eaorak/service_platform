package com.adenon.sp.service.test;

import com.adenon.channel.sss.api.message.SmsSendMessage;
import com.adenon.channel.sss.api.operation.ISmsProvider;
import com.adenon.sp.services.service.ServiceActivator;
import com.adenon.sp.services.service.factory.IServiceFactory;
import com.adenon.sp.services.service.factory.SingletonServiceFactory;


public class SmsServiceActivator extends ServiceActivator {

    @Override
    public void startService() throws Exception {
        this.sendTestMessages();
    }

    private void sendTestMessages() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread() {

                @Override
                public void run() {
                    while (true) {
                        ISmsProvider smsProvider = SmsServiceActivator.this.getService(ISmsProvider.class);
                        SmsSendMessage sendSms = new SmsSendMessage();
                        sendSms.setConnectionName("c1");
                        sendSms.setConnectionGroupName("g1");
                        sendSms.setMessage("sms message");
                        try {
                            smsProvider.sendSms(sendSms, new SmsTestHandler());
                            Thread.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };// .start();
        }
    }

    @Override
    public void stopService() throws Exception {
    }

    @Override
    protected IServiceFactory getServiceFactory() {
        return new SingletonServiceFactory();
    }

}
