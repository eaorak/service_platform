package com.adenon.api.smpp.core;

import java.nio.ByteBuffer;

import com.adenon.api.smpp.buffer.SendBufferObject;
import com.adenon.api.smpp.buffer.SmppBufferManager;


public class SmmpIOReader implements Runnable {

    private final IIOReactor ioReactor;

    public SmmpIOReader(final IIOReactor ioReactor) {
        this.ioReactor = ioReactor;
    }

    @Override
    public void run() {
        try {
            if (!this.ioReactor.getConnectionInformation().getConnectionState().isStopped()) {
                final SendBufferObject nextBufferObject = SmppBufferManager.getNextBufferObject();
                if (nextBufferObject != null) {
                    try {
                        final ByteBuffer readSmppPackage = this.ioReactor.getSmppPackageReader().readSmppPackage(nextBufferObject.getByteBuffer(),
                                                                                                                 this.ioReactor);
                        if (readSmppPackage == null) {
                            this.ioReactor.getLogger().error("SmmpIOReader",
                                                             "run",
                                                             0,
                                                             null,
                                                             " : Error : buffer returned empty after reading socket. Exiting from thread.");
                            return;
                        }
                        this.ioReactor.getMessageHandler().handleMsg(readSmppPackage);
                    } catch (final Exception e) {
                        this.ioReactor.getLogger().error("SmmpIOReader", "run", 0, this.ioReactor.getLabel(), " : Error : " + e.getMessage(), e);
                        this.ioReactor.closeConnection("Error : " + e.getMessage());
                    } finally {
                        SmppBufferManager.releaseBufferObject(nextBufferObject);
                    }
                } else {
                    this.ioReactor.getLogger().error("SmmpIOReader",
                                                     "run",
                                                     0,
                                                     this.ioReactor.getLabel(),
                                                     " : Error : Couldnt get a valid byte buffer for reading process.");
                }
            }
        } catch (final Exception e) {
            this.ioReactor.getLogger().error("SmmpIOReader", "run", 0, null, " : Error : " + e.getMessage(), e);
        } finally {
            this.ioReactor.decreaseThreadCount();
        }
    }
}
