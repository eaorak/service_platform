package com.adenon.api.smpp.messaging.processor;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.adenon.api.smpp.buffer.SendBufferObject;
import com.adenon.api.smpp.buffer.SmppBufferManager;
import com.adenon.api.smpp.common.SequenceGenerator;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.core.ResponseHandler;
import com.adenon.api.smpp.sdk.IMessage;
import com.adenon.api.smpp.sdk.SIActionType;
import com.adenon.api.smpp.sdk.WapPushBookmarkMessageDescriptor;
import com.adenon.api.smpp.sdk.WapPushSIMessageDescriptor;
import com.adenon.api.smpp.sdk.WapPushSLMessageDescriptor;
import com.adenon.api.smpp.wappush.BookmarkMessage;
import com.adenon.api.smpp.wappush.IWapMessage;
import com.adenon.api.smpp.wappush.ServiceIndicationMessage;
import com.adenon.api.smpp.wappush.ServiceLoadingMessage;
import com.adenon.api.smpp.wappush.WDPPart;


public class WapPushProcessor extends ResponseHandler {


    private IWapMessage                            wapPushMessage;
    private final ArrayList<MessagePartDescriptor> concatMessagePointer = new ArrayList<MessagePartDescriptor>(10);


    public WapPushProcessor(IMessage messageDescriptor) throws Exception {
        final SendBufferObject nextBufferObject = SmppBufferManager.getNextBufferObject();
        if (nextBufferObject == null) {
            throw new SmppApiException(SmppApiException.FATAL_ERROR, "Buffer object is null");
        }
        try {
            switch (messageDescriptor.getMessageType()) {
                case WAPPushSI:
                    WapPushSIMessageDescriptor indicatorDescriptor = (WapPushSIMessageDescriptor) messageDescriptor;
                    this.wapPushMessage = new ServiceIndicationMessage(indicatorDescriptor);
                    break;
                case WAPPushSL:
                    WapPushSLMessageDescriptor slMessageDescriptor = (WapPushSLMessageDescriptor) messageDescriptor;
                    this.wapPushMessage = new ServiceLoadingMessage(slMessageDescriptor);
                    break;
                case WAPBookmark:
                    WapPushBookmarkMessageDescriptor bookmarkMessageDescriptor = (WapPushBookmarkMessageDescriptor) messageDescriptor;
                    this.wapPushMessage = new BookmarkMessage(bookmarkMessageDescriptor);
                    break;
            }
            int messageCount = 0;
            ByteBuffer byteBuffer = nextBufferObject.getByteBuffer();
            this.wapPushMessage.encode(byteBuffer);
            int wspBytesLength = this.wapPushMessage.getWSPBytesLength();
            int messageLength = byteBuffer.position();
            int concatCapacityForFirstPart = WDPPart.getTotalBytesLength() - WDPPart.getUdhConcatBytesLength() - wspBytesLength;
            int concatCapacityForOtherParts = WDPPart.getTotalBytesLength() - WDPPart.getUdhConcatBytesLength();
            if ((messageLength + WDPPart.getUdhBytesLength() + wspBytesLength) > WDPPart.getTotalBytesLength()) {
                messageCount = (messageLength / concatCapacityForFirstPart);
                int leftOver = messageLength % concatCapacityForFirstPart;
                if (leftOver > 0) {
                    messageCount++;
                }
            } else {
                messageCount = 1;
            }
            int startIndex = 0;
            int amount = concatCapacityForFirstPart;
            int endIndex;
            for (int i = 0; i < messageCount; i++) {
                if (i > 0) {
                    amount = concatCapacityForOtherParts;
                }
                if ((startIndex + amount) < messageLength) {
                    endIndex = startIndex + amount;
                } else {
                    endIndex = messageLength;
                }
                final MessagePartDescriptor messagePartDescriptor = new MessagePartDescriptor();
                messagePartDescriptor.setStart(startIndex);
                messagePartDescriptor.setEnd(endIndex);
                messagePartDescriptor.setLength((endIndex - startIndex));
                byte[] myBytes = new byte[messagePartDescriptor.getLength()];
                byteBuffer.position(startIndex);
                byteBuffer.get(myBytes, 0, messagePartDescriptor.getLength());
                messagePartDescriptor.setByteArray(myBytes);
                this.concatMessagePointer.add(messagePartDescriptor);
                startIndex += amount;
            }
            this.createHandler(messageCount);
        } finally {
            SmppBufferManager.releaseBufferObject(nextBufferObject);
        }
    }

    @Override
    public void fillMessageBody(ByteBuffer byteBuffer,
                                int index,
                                byte[] concatHeader) throws Exception {
        int messageIndex = 0;
        if (this.getMessagePartCount() > 1) {
            messageIndex = SequenceGenerator.getNextRefNumByte();
        }

        final MessagePartDescriptor messagePartDescriptor = this.concatMessagePointer.get(index);
        int lengthpos = byteBuffer.position();
        byteBuffer.put((byte) (0xff));
        if (this.getMessagePartCount() == 1) {
            this.wapPushMessage.encodeUDHBytes(byteBuffer);
        } else {
            this.wapPushMessage.encodeUDHBytes(byteBuffer, this.getMessagePartCount(), index, messageIndex);
        }
        if (index == 0) {
            this.wapPushMessage.encodeWSPBytes(byteBuffer);
        }

        byte[] byteArray = messagePartDescriptor.getByteArray();
        byteBuffer.put(byteArray);
        int lastPosition = byteBuffer.position();
        byteBuffer.position(lengthpos);
        byteBuffer.put((byte) ((lastPosition - lengthpos - 1) & 0xff));
        byteBuffer.position(lastPosition);

    }

    public static void main(String[] args) {
        try {
            WapPushSIMessageDescriptor indicatorDescriptor = new WapPushSIMessageDescriptor(SIActionType.SignalLow);
            indicatorDescriptor.setCreationDate(0);
            indicatorDescriptor.setHrefUrl("http://wap.com.tr/demo1/demo2/demo3/demo4/demo5/demo6/demo7/demo8/demo9/demo10/demo1/demo2/demo3/demo4/demo5/demo6/demo7/demo8/demo9/demo10");
            indicatorDescriptor.setServiceIndicatorId("1120");
            indicatorDescriptor.setActionType(SIActionType.SignalLow);
            indicatorDescriptor.setSiExpiryDate(0);
            indicatorDescriptor.setText("This is the demo /demo1/demo2/demo3/demo4/demo5/demo6/demo7/demo8/demo9/demo10/demo1/demo2/demo3/demo4/demo5/demo6/demo7/demo8/demo9/demo10");
            WapPushProcessor pushProcessor = new WapPushProcessor(indicatorDescriptor);
            for (int i = 0; i < pushProcessor.getMessagePartCount(); i++) {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                pushProcessor.fillMessageBody(byteBuffer, i, null);
                System.out.println(byteBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
