package com.adenon.sp.channel.test;

import com.adenon.sp.channel.test.api.TestMessageTypes;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.MessageType;


public class TestMessage extends AbstractMessage {

    public TestMessage() {
        super(MessageType.BEGIN);
    }

    @Override
    public TestMessageTypes getId() {
        return TestMessageTypes.TEST_MESSAGE;
    }

}
