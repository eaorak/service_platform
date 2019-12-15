package com.adenon.smpp.server.core;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.adenon.api.smpp.buffer.SendBufferObject;
import com.adenon.api.smpp.buffer.SmppBufferManager;
import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.SequenceGenerator;
import com.adenon.api.smpp.common.Smpp34Constants;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.common.TransactionManager;
import com.adenon.api.smpp.core.ConnectionController;
import com.adenon.api.smpp.core.IOReactor;
import com.adenon.api.smpp.core.ISmppMessageHandler;
import com.adenon.api.smpp.core.buffer.BufferBean;
import com.adenon.api.smpp.core.buffer.ResponseBufferImplementation;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.message.BindResponseMessage;
import com.adenon.api.smpp.message.DeliverSMMessage;
import com.adenon.api.smpp.message.DeliverSMResponseMessage;
import com.adenon.api.smpp.message.MessageHeader;
import com.adenon.api.smpp.message.SubmitSMResponseMessage;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.api.smpp.sdk.ESendResult;
import com.adenon.api.smpp.sdk.SmppConnectionType;

public class ServerIOReactor extends IOReactor {

    public final static long    MAX_NUMBER     = 99999999999999L;
    public final static long    MIN_NUMBER     = 1;

    private ISmppMessageHandler messageHandler = new ServerMessageHandler(this);

    private IServerCallback     serverCallback;


    private SocketChannel       socketChannel;

    private static long         ack_num        = 0;

    private ServerApiDelegator  smppApiDelegator;
    private SmppConnectionType  bindType       = SmppConnectionType.BOTH;
    private final String        serverName;
    private String              externalConnectionName;

    public ServerIOReactor(LoggerWrapper logger,
                           String serverName,
                           ServerApiDelegator smppApiDelegator,
                           SocketChannel socketChannel,
                           String ip,
                           int port) {
        super(logger);
        this.serverName = serverName;
        this.setSmppApiDelegator(smppApiDelegator);
        this.setConnectionInformation(new ConnectionInformation(this, serverName, "connectionState"));
        this.getConnectionInformation().setIp(ip);
        this.getConnectionInformation().setPort(port);
        this.setIoReactorLock(new Object());
        this.setShutdown(false);
        this.socketChannel = socketChannel;
        this.setLabel("[" + serverName + "@connectionState]");


    }


    public void initialize() throws SmppApiException {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("ServerIOReactor",
                                   "initialize",
                                   0,
                                   this.getLabel(),
                                   "Server IO Reactor is initializing. --> " + this.getConnectionInformation().toString());
        }
        this.setResponseBuffer(new ResponseBufferImplementation(this.getSmppApiDelegator().getApiProperties().getWindowSize(),
                                                                2,
                                                                this.getLogger(),
                                                                10000,
                                                                this.getLabel()));

        this.setServerCallback(this.getSmppApiDelegator().getServerCallback());

        this.setConnectionController(new ConnectionController(this));
        this.getConnectionController().setDaemon(true);
        this.getConnectionController().start();
        this.getConnectionInformation().getConnectionState().suspended();

    }


    public void cleanupConnection(String reason) {
        try {
            try {
                this.getSmppApiDelegator().getServerConnectionStore().remove(this.getExternalConnectionName());
            } catch (Exception e) {
            }
            try {
                this.smppApiDelegator.getSmppIOReactorStorage().remove(this);
            } catch (Exception e) {
            }

            try {
                if (this.getSocketChannel() != null) {
                    this.getSocketChannel().socket().close();
                }
            } catch (Exception e) {
            }
            try {
                if (this.getSocketChannel() != null) {
                    this.getSocketChannel().close();
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            this.getLogger().error("ServerIOReactor", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
        } finally {
            try {
                this.getConnectionInformation().setConnected(false);
                this.setSocketChannel(null);
                this.getConnectionInformation().getConnectionState().stopped();
                this.getResponseBuffer().resetBuffer();
                this.serverCallback.disconnected(this.getConnectionInformation());
            } catch (Exception e) {
                this.getLogger().error("ServerIOReactor", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
            } finally {
                this.getBinded().set(false);
                this.setThreadCount(0);
            }
        }
    }

    @Override
    public void closeConnection(String description) {
        synchronized (this.getIoReactorLock()) {
            if ((!this.getConnectionInformation().getConnectionState().isStopped()) || (this.isShutdown()) || (this.getConnectionInformation().isConnected())) {
                if (this.getLogger().isInfoEnabled()) {
                    this.getLogger().info("ServerIOReactor", "closeConnection", 0, this.getLabel(), "#CLOSING CONNECTION# Description : " + description);
                }
                try {
                    this.nackToWaitingObjects();
                } catch (Exception e) {
                }
                this.cleanupConnection(description);
            }
        }
    }

    public void nackToWaitingObjects() {
        for (int i = 0; i < this.getResponseBuffer().getBufferBeans().length; i++) {
            try {
                if ((this.getResponseBuffer().getBufferBeans()[i].getStatus().get() == BufferBean.OBJECT_STATUS_READABLE)
                    && (this.getResponseBuffer().getBufferBeans()[i].getSequenceNumber() > 0)) {
                    if (this.getResponseBuffer().getBufferBeans()[i].getWaitingObject() != null) {
                        if (this.getResponseBuffer().getBufferBeans()[i].getWaitingObject().getMesssageType() == Smpp34Constants.MSG_DELIVER_SM) {
                            DeliverSMMessage deliverSM = (DeliverSMMessage) this.getResponseBuffer().getBufferBeans()[i].getWaitingObject();
                            deliverSM.setSendResult(ESendResult.RETRY);
                            if (deliverSM.getWaitObject() == null) {
                                this.serverCallback.deliveryResult(this.getConnectionInformation(), deliverSM, deliverSM.getAttachedObject());
                            } else {
                                synchronized (deliverSM.getWaitObject()) {
                                    deliverSM.getWaitObject().notify();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                this.getLogger().error("ServerIOReactor", "nackToWaitingObjects", 0, null, " : Error : " + e.getMessage(), e);
            }
            try {
                this.getResponseBuffer().getBufferBeans()[i].release();
            } catch (Exception e) {
                this.getLogger().error("ServerIOReactor", "nackToWaitingObjects", 0, null, " : Error : " + e.getMessage(), e);
            }
        }
    }

    public long sendDeliverSM(DeliverSMMessage deliverSM,
                              Object returnObject) throws Exception {

        deliverSM.init(returnObject);
        int messageCount = deliverSM.getMessageProcessor().getMessagePartCount();
        if (messageCount > 0) {
            for (int i = 0; i < messageCount; i++) {
                int sequenceNumber = this.getSequenceNumber();
                if (!deliverSM.isDelivery()) {
                    byte referenceNumber = this.getNextRefNumByte();
                    deliverSM.setOpParamSarMsgRefNum(referenceNumber);
                    deliverSM.setOpParamSarSegmentSequenceNum(i + 1);
                    deliverSM.setOpParamSarTotalSegments(messageCount);
                }
                SendBufferObject nextBufferObject = SmppBufferManager.getNextBufferObject();
                if (nextBufferObject == null) {
                    throw new SmppApiException(SmppApiException.FATAL_ERROR, "No send buffer");
                }
                try {
                    deliverSM.getMessageProcessor().addSequence(i, sequenceNumber);
                    deliverSM.fillBuffer(nextBufferObject.getByteBuffer(), sequenceNumber, i);
                    this.sendMsg(deliverSM, sequenceNumber, nextBufferObject.getByteBuffer());
                } catch (Exception e) {
                    this.getLogger().error("ServerIOReactor", "sendSubmitSM", 0, null, " : Error : " + e.getMessage(), e);
                    throw new SmppApiException(e);
                } finally {
                    SmppBufferManager.releaseBufferObject(nextBufferObject);
                }
            }
        }
        return deliverSM.getTransactionId();
    }


    public void sendSubmitSMResponse(int seqno,
                                     String messageId,
                                     ByteBuffer byteBuffer,
                                     int status) throws Exception {
        SubmitSMResponseMessage submitSMResponse = new SubmitSMResponseMessage();
        submitSMResponse.setMessageIdentifier(messageId);
        byteBuffer.clear();
        CommonUtils.createHeader(byteBuffer, Smpp34Constants.MSG_SUBMIT_SM_RESP, seqno, status);
        submitSMResponse.fillBody(byteBuffer);
        CommonUtils.setLength(byteBuffer);
        this.writeBuffer(byteBuffer);
    }

    public void sendDeliverSMResponse(int seqno,
                                      ByteBuffer byteBuffer,
                                      int status) throws Exception {
        DeliverSMResponseMessage deliverSMResponse = new DeliverSMResponseMessage();
        byteBuffer.clear();
        CommonUtils.createHeader(byteBuffer, Smpp34Constants.MSG_DELIVER_SM_RESP, seqno, status);
        deliverSMResponse.fillBody(byteBuffer);
        CommonUtils.setLength(byteBuffer);
        this.writeBuffer(byteBuffer);
    }

    public void sendBindResponse(MessageHeader smpp34Header,
                                 ByteBuffer byteBuffer,
                                 BindResponseMessage bindResponseMessage,
                                 int status) throws Exception {
        byteBuffer.clear();
        int responseType = Smpp34Constants.MSG_BIND_TRANSCVR_RESP;
        switch (smpp34Header.getCommandID()) {
            case Smpp34Constants.MSG_BIND_TRANSCVR:
                responseType = Smpp34Constants.MSG_BIND_TRANSCVR_RESP;
                break;
            case Smpp34Constants.MSG_BIND_TRANSMITTER:
                responseType = Smpp34Constants.MSG_BIND_TRANSMITTER_RESP;
                break;
            case Smpp34Constants.MSG_BIND_RECEIVER:
                responseType = Smpp34Constants.MSG_BIND_RECEIVER_RESP;
                break;
            default:
                responseType = Smpp34Constants.MSG_BIND_TRANSCVR_RESP;
                break;

        }
        CommonUtils.createHeader(byteBuffer, responseType, smpp34Header.getSequenceNo(), status);
        bindResponseMessage.fillBody(byteBuffer);
        CommonUtils.setLength(byteBuffer);
        this.writeBuffer(byteBuffer);
    }

    @Override
    public int getSequenceNumber() {
        return SequenceGenerator.getNextSequenceNum();
    }

    @Override
    public short getNextRefNum() {
        return SequenceGenerator.getNextRefNum();
    }

    @Override
    public byte getNextRefNumByte() {
        return SequenceGenerator.getNextRefNumByte();
    }

    @Override
    public void sendAlive(int seq) throws Exception {
        ByteBuffer enqLink = ByteBuffer.allocateDirect(30);
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("ServerIOReactor", "sendAlive", 0, this.getLabel(), " Sending Enquire Link . Sequence : " + seq);
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

    public synchronized static long getNextAckNum() {
        ack_num++;
        if (ack_num > MAX_NUMBER) {
            ack_num = MIN_NUMBER;
        }
        return ack_num;
    }

    public DeliverSMMessage createDeliverSMMessage() {
        DeliverSMMessage deliverSMMessage = new DeliverSMMessage(this.getLogger(), TransactionManager.getNextTransactionID(), this.getLabel());
        return deliverSMMessage;
    }

    public DeliverSMMessage createDeliverSMMessage(long transactionId) {
        DeliverSMMessage deliverSMMessage = new DeliverSMMessage(this.getLogger(), transactionId, this.getLabel());
        return deliverSMMessage;
    }

    public void restartConnection() {
        this.closeConnection("Restart command received.");
    }

    public int getWindowSize() {
        return this.getResponseBuffer().getBufferSize();
    }

    public int getSmsType(int datacoding) {
        return 0;
    }

    public int getUsedBufferCount() {
        return this.getResponseBuffer().getUsedItemCount();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // TODO : implement here
        return builder.toString();
    }


    public IServerCallback getServerCallback() {
        return this.serverCallback;
    }

    public void setServerCallback(IServerCallback smppCallback) {
        this.serverCallback = smppCallback;
    }


    @Override
    public ISmppMessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    @Override
    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }


    @Override
    public int getMaxThreadCount() {
        return this.getSmppApiDelegator().getApiProperties().getMaxThreadCount();
    }

    @Override
    public boolean handleCloseConnection() {
        this.closeConnection("Connection is closed");

        return true;
    }

    @Override
    public void handleTimeoutRequests() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isTraceON() {
        return this.getSmppApiDelegator().getApiProperties().isTraceOn();
    }

    public SmppConnectionType getBindType() {
        return this.bindType;
    }

    public void setBindType(int bindType) {
        switch (bindType) {
            case Smpp34Constants.MSG_BIND_RECEIVER:
                this.bindType = SmppConnectionType.READ;
                break;
            case Smpp34Constants.MSG_BIND_TRANSCVR:
                this.bindType = SmppConnectionType.BOTH;
                break;
            case Smpp34Constants.MSG_BIND_TRANSMITTER:
                this.bindType = SmppConnectionType.WRITE;
                break;
            default:
                this.bindType = SmppConnectionType.BOTH;
                break;
        }
    }


    @Override
    public void handleNack(BufferBean bufferBean,
                           MessageHeader smpp34Header) {
        if (bufferBean.getWaitingObject().getMesssageType() == Smpp34Constants.MSG_DELIVER_SM) {
            if (bufferBean.getWaitingObject() != null) {
                final DeliverSMMessage deliverSM = (DeliverSMMessage) bufferBean.getWaitingObject();
                deliverSM.setSendResult(ESendResult.FATAL_ERROR);
                deliverSM.getMessageProcessor().errorReceived();
                if (deliverSM.getWaitObject() == null) {
                    this.getServerCallback().deliveryResult(this.getConnectionInformation(), deliverSM, deliverSM.getAttachedObject());
                } else {
                    synchronized (deliverSM.getWaitObject()) {
                        deliverSM.getWaitObject().notify();
                    }
                }
            }
        }
    }

    public String getServerName() {
        return this.serverName;
    }

    public ServerApiDelegator getSmppApiDelegator() {
        return this.smppApiDelegator;
    }

    public void setSmppApiDelegator(ServerApiDelegator smppApiDelegator) {
        this.smppApiDelegator = smppApiDelegator;
    }

    public String getExternalConnectionName() {
        return this.externalConnectionName;
    }

    public void setExternalConnectionName(String externalConnectionName) {
        this.externalConnectionName = externalConnectionName;
    }


    @Override
    public boolean isBinded() {
        return this.getBinded().get();
    }

}
