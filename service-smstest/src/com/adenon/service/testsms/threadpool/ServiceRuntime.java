package com.adenon.service.testsms.threadpool;

import com.adenon.channel.sms.api.operation.ISmsSender;
import com.adenon.service.testsms.container.SendWorker;
import com.adenon.service.testsms.handler.SmsHandler;
import com.adenon.sp.executer.task.pool.IThreadPool;



public class ServiceRuntime extends Thread {


    private final IThreadPool threadPool;
    private SmsHandler        handler = new SmsHandler();
    private final ISmsSender  smsSender;


    public ServiceRuntime(IThreadPool threadPool,
                          ISmsSender smsSender) {
        this.threadPool = threadPool;
        this.smsSender = smsSender;
    }

    @Override
    public void run() {
        SendWorker sendWorker = new SendWorker(this.smsSender, this.handler);
        this.threadPool.execute(sendWorker);
    }
}
