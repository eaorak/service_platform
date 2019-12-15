package com.adenon.api.smpp.sdk;


public class WapPushBookmarkMessageDescriptor implements IMessage {

    private String name;
    private String url;

    public String getName() {
        return this.name;
    }


    public WapPushBookmarkMessageDescriptor setName(String name) {
        this.name = name;
        return this;
    }


    public String getUrl() {
        return this.url;
    }


    public WapPushBookmarkMessageDescriptor setUrl(String url) {
        this.url = url;
        return this;
    }


    @Override
    public EMessageType getMessageType() {
        return EMessageType.WAPBookmark;
    }

    public static WapPushBookmarkMessageDescriptor getDefaultWapPushBookmarkMessageDescriptor(String name,
                                                                                              String url) {
        return new WapPushBookmarkMessageDescriptor().setName(name).setUrl(url);
    }

}
