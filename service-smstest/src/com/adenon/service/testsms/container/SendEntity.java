package com.adenon.service.testsms.container;

import com.adenon.channel.sms.api.message.SmsSendMessage;


public class SendEntity extends SmsSendMessage {

    private int msgId;
    private int msgCount;
    private int sendStatus;

    public int getMsgId() {
        return this.msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgCount() {
        return this.msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }


}
