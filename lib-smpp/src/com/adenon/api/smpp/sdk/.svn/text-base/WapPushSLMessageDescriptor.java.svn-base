package com.adenon.api.smpp.sdk;


public class WapPushSLMessageDescriptor extends WapServiceDescriptor implements IMessage {


    public WapPushSLMessageDescriptor(SLActionType slActionType) {
        this.setActionType(slActionType);
    }


    @Override
    public EMessageType getMessageType() {
        return EMessageType.WAPPushSL;
    }

    public final static WapPushSLMessageDescriptor getDefaultWapPushSL(String url) {
        WapPushSLMessageDescriptor messageDescriptor = new WapPushSLMessageDescriptor(SLActionType.ExecuteLow);
        messageDescriptor.setHrefUrl(url);
        return messageDescriptor;
    }


}
