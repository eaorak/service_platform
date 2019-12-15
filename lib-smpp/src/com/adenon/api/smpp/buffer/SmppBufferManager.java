package com.adenon.api.smpp.buffer;

import java.util.concurrent.ConcurrentLinkedQueue;


public class SmppBufferManager {

    private static ConcurrentLinkedQueue<SendBufferObject> bufferQueue  = new ConcurrentLinkedQueue<SendBufferObject>();
    public static final int                                MAX_PDU_SIZE = 1024;

    static {
        init();
    }

    private SmppBufferManager() {
    }

    public static void init() {
        SendBufferObject temp;
        for (int i = 0; i < 1000; i++) {
            temp = new SendBufferObject();
            bufferQueue.add(temp);
        }
    }

    public static SendBufferObject getNextBufferObject() {
        SendBufferObject bufferObject = bufferQueue.poll();
        if (bufferObject == null) {
            bufferObject = new SendBufferObject();
        }
        return bufferObject;
    }

    public static void releaseBufferObject(final SendBufferObject bufferObject) {
        if (bufferObject == null) {
            return;
        }
        bufferObject.getByteBuffer().clear();
        bufferQueue.add(bufferObject);
    }

    public static void releaseBufferObjects(final SendBufferObject[] bufferObject) {
        if (bufferObject == null) {
            return;
        }
        for (int i = 0; i < bufferObject.length; i++) {
            if (bufferObject[i] != null) {
                bufferObject[i].getByteBuffer().clear();
                bufferQueue.add(bufferObject[i]);
            }
        }
    }
}
