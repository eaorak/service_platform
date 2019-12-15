package com.adenon.api.smpp.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.PDUParser;
import com.adenon.api.smpp.common.SequenceGenerator;
import com.adenon.api.smpp.common.Smpp34Constants;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.core.buffer.BufferBean;
import com.adenon.api.smpp.core.buffer.ResponseBufferImplementation;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.message.MessageObject;
import com.adenon.api.smpp.sdk.ConnectionInformation;


public abstract class IOReactor implements IIOReactor {

    private LoggerWrapper                logger;

    private AtomicBoolean                binded             = new AtomicBoolean(false);

    private int                          bufferFullHitCount = 0;

    private StatisticCollector           statisticCollector = new StatisticCollector();

    private SmppPackageReader            smppPackageReader  = new SmppPackageReader();

    private ConnectionInformation        connectionInformation;
    private ConnectionController         connectionController;

    private boolean                      shutdown           = false;

    private long                         startWriteTime     = 0;
    private boolean                      writeEnd           = true;
    private long                         lastWriteTime      = System.currentTimeMillis();
    private long                         lastReadTime       = System.currentTimeMillis();

    private Object                       ioReactorLock;
    private Object                       readLock           = new Object();
    private Object                       writeLock          = new Object();

    private String                       label;

    private int                          threadCount        = 0;
    private Object                       threadSync         = new Object();

    private long                         suspendStartTime   = 0;


    private ResponseBufferImplementation responseBuffer;


    public IOReactor(LoggerWrapper logger) {
        this.logger = logger;
    }

    protected void sendMsg(MessageObject message,
                           int sequenceNumber,
                           ByteBuffer buffer) throws Exception {
        if (!this.getConnectionInformation().getConnectionState().isIdle()) {
            throw new SmppApiException(SmppApiException.RETRY, SmppApiException.DOMAIN_IOREACTOR, "IO Reactor is not ready please retry again");
        }
        BufferBean bufferBean;
        bufferBean = this.getResponseBuffer().getFreeItem(sequenceNumber);
        if (bufferBean == null) {
            long retryCount = 0;
            while ((bufferBean == null) && (retryCount < 10)) {
                retryCount++;
                try {
                    Thread.sleep(retryCount + 5L);
                } catch (InterruptedException e) {
                }
                bufferBean = this.getResponseBuffer().getFreeItem(sequenceNumber);
            }
        }
        if (bufferBean != null) {
            this.bufferFullHitCount = 0;
            bufferBean.setWaitingObject(message);
            bufferBean.setSequenceNumber(sequenceNumber);
            bufferBean.setUseDate(System.currentTimeMillis());
            bufferBean.getStatus().set(BufferBean.OBJECT_STATUS_READABLE);
            this.writeBuffer(buffer);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("IOReactor", "sendMsg", 0, this.getLabel(), " sequence : " + sequenceNumber + " " + message.getDescription());
            }
        } else {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("IOReactor", "sendMsg", 0, this.getLabel(), "Window Buffer is full for sequence :  " + sequenceNumber);
            }
            this.bufferFullHitCount++;
            if (this.bufferFullHitCount > 20) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("IOReactor", "sendMsg", 0, this.getLabel(), " 20 times buffer return null. Suspending. ");
                }
                this.getConnectionInformation().getConnectionState().suspended();
                this.setSuspendStartTime(System.currentTimeMillis());
                throw new SmppApiException(SmppApiException.RETRY,
                                           SmppApiException.DOMAIN_IOREACTOR,
                                           "Buffer is full for long time and closing connection. Please try again");
            }
            throw new SmppApiException(SmppApiException.RETRY, SmppApiException.DOMAIN_IOREACTOR, "Buffer is full please try again");
        }
    }


    protected void writeBuffer(ByteBuffer buffer) throws IOException {
        buffer.flip();
        this.setStartWriteTime(System.currentTimeMillis());
        synchronized (this.writeLock) {
            this.setWriteEnd(false);
            this.getSocketChannel().write(buffer);
            this.setWriteEnd(true);
        }
        if (this.isTraceON()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("IOReactor",
                                  "writeBuffer",
                                  0,
                                  this.getLabel(),
                                  " [SEND PDU] : " + CommonUtils.bytesToHexFormated(buffer) + "\n" + PDUParser.parsePDU(buffer));
            }
        }

        this.setLastWriteTime(System.currentTimeMillis());
        this.statisticCollector.increaseTotalSentPackageCount();
    }

    @Override
    public void sendAlive(int seq) throws Exception {
        ByteBuffer enqLink = ByteBuffer.allocateDirect(30);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("IOReactor", "sendAlive", 0, this.getLabel(), " Sending Enquire Link . Sequence : " + seq);
        }
        CommonUtils.createAckHeader(enqLink, Smpp34Constants.MSG_ENQUIRE_LINK, seq, 0);
        this.writeBuffer(enqLink);
        enqLink = null;
    }

    @Override
    public void sendAliveRes(int seq) throws Exception {
        ByteBuffer enqLink = ByteBuffer.allocate(20);
        CommonUtils.createAckHeader(enqLink, Smpp34Constants.MSG_ENQUIRE_LINK_RESP, seq, 0);
        this.writeBuffer(enqLink);
        enqLink = null;
    }

    @Override
    public void shutdown() throws Exception {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("IOReactor", "shutdown", 0, this.getLabel(), "*SHUTDOWN* Initiated : " + this.toString());
        }
        this.setShutdown(true);
        this.getConnectionInformation().getConnectionState().stopped();
    }

    public void close() {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("IOReactor", "close", 0, this.getLabel(), "*CLOSE* function called" + this.toString());
        }
        this.closeConnection("Close called from interface.");
    }

    public boolean checkConnectionSanity() {
        return this.getConnectionInformation().getConnectionState().isIdle();
    }

    @Override
    public long getStartWriteTime() {
        return this.startWriteTime;
    }

    @Override
    public void setStartWriteTime(long startWriteTime) {
        this.startWriteTime = startWriteTime;
    }

    @Override
    public boolean isWriteEnd() {
        return this.writeEnd;
    }

    public void setWriteEnd(boolean writeEnd) {
        this.writeEnd = writeEnd;
    }

    @Override
    public ConnectionInformation getConnectionInformation() {
        return this.connectionInformation;
    }

    @Override
    public ResponseBufferImplementation getResponseBuffer() {
        return this.responseBuffer;
    }

    public boolean isShutdown() {
        return this.shutdown;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }


    public void setResponseBuffer(ResponseBufferImplementation responseBuffer) {
        this.responseBuffer = responseBuffer;
    }

    @Override
    public int getSequenceNumber() {
        return SequenceGenerator.getNextSequenceNum();
    }

    public short getNextRefNum() {
        return SequenceGenerator.getNextRefNum();
    }

    public byte getNextRefNumByte() {
        return SequenceGenerator.getNextRefNumByte();
    }

    @Override
    public void setLogger(LoggerWrapper logger) {
        this.logger = logger;
    }

    @Override
    public SmppPackageReader getSmppPackageReader() {
        return this.smppPackageReader;
    }

    @Override
    public long getLastReadTime() {
        return this.lastReadTime;
    }

    @Override
    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Override
    public void increaseThreadCount() {
        synchronized (this.threadSync) {
            this.setThreadCount(this.getThreadCount() + 1);
        }
    }

    @Override
    public void decreaseThreadCount() {
        synchronized (this.threadSync) {
            this.setThreadCount(this.getThreadCount() - 1);
            if (this.getThreadCount() < 0) {
                this.setThreadCount(0);
            }
        }
    }

    @Override
    public int getThreadCount() {
        synchronized (this.threadSync) {
            return this.threadCount;
        }
    }

    @Override
    public Object getReadLock() {
        return this.readLock;
    }

    @Override
    public long getSuspendStartTime() {
        return this.suspendStartTime;
    }

    @Override
    public void setSuspendStartTime(long suspendStartTime) {
        this.suspendStartTime = suspendStartTime;
    }

    @Override
    public long getLastWriteTime() {
        return this.lastWriteTime;
    }

    @Override
    public void setLastWriteTime(long lastActivityTime) {
        this.lastWriteTime = lastActivityTime;
    }

    @Override
    public StatisticCollector getStatisticCollector() {
        return this.statisticCollector;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public Object getIoReactorLock() {
        return this.ioReactorLock;
    }

    public void setIoReactorLock(Object ioReactorLock) {
        this.ioReactorLock = ioReactorLock;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void setConnectionInformation(ConnectionInformation connectionInformation) {
        this.connectionInformation = connectionInformation;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public ConnectionController getConnectionController() {
        return this.connectionController;
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    @Override
    public LoggerWrapper getLogger() {
        return this.logger;
    }

    public AtomicBoolean getBinded() {
        return this.binded;
    }

    public void setBinded(AtomicBoolean binded) {
        this.binded = binded;
    }


}
