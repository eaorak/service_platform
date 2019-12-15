package com.adenon.sp.eee.dispatch.test;

public class Message {

    private InnerMessage msg    = new InnerMessage();
    private String       msgKey = "key";

    public Message() {
    }

    public void setMsg(InnerMessage msg) {
        this.msg = msg;
    }

    public InnerMessage getMsg() {
        return this.msg;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgKey() {
        return this.msgKey;
    }

    @Override
    public String toString() {
        return "Message [msg=" + this.msg + ", msgKey=" + this.msgKey + "]";
    }

}
