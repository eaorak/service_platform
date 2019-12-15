package com.adenon.api.smpp.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.adenon.api.smpp.buffer.SendBufferObject;
import com.adenon.api.smpp.buffer.SmppBufferManager;
import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.SequenceGenerator;
import com.adenon.api.smpp.common.Smpp34Constants;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.common.StateHigherAuthority;
import com.adenon.api.smpp.common.TransactionManager;
import com.adenon.api.smpp.core.buffer.BufferBean;
import com.adenon.api.smpp.core.buffer.ResponseBufferImplementation;
import com.adenon.api.smpp.core.handler.SmppMessageHandler;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.message.BindRequestMessage;
import com.adenon.api.smpp.message.DeliverSMResponseMessage;
import com.adenon.api.smpp.message.MessageHeader;
import com.adenon.api.smpp.message.Smpp34CancelSM;
import com.adenon.api.smpp.message.SubmitSMMessage;
import com.adenon.api.smpp.message.SubmitSMResponseMessage;
import com.adenon.api.smpp.sdk.AddressDescriptor;
import com.adenon.api.smpp.sdk.AlarmCode;
import com.adenon.api.smpp.sdk.ConnectionDescriptor;
import com.adenon.api.smpp.sdk.ConnectionInformation;
import com.adenon.api.smpp.sdk.ESendResult;
import com.adenon.api.smpp.sdk.ISmppCallback;
import com.adenon.api.smpp.sdk.SmppConnectionType;

public class SmppIOReactor extends IOReactor {

    public final static long     MAX_NUMBER     = 99999999999999L;
    public final static long     MIN_NUMBER     = 1;

    private ISmppMessageHandler  messageHandler = new SmppMessageHandler(this);
    private int                  order          = 0;
    private ISmppCallback        smppCallback;


    private SocketChannel        socketChannel  = null;

    private static long          ackNumber      = 0;

    private ConnectionDescriptor connectionDescriptor;
    private SmppApiDelegator     smppApiDelegator;


    public SmppIOReactor(LoggerWrapper logger,
                         String connectionGroupName,
                         ConnectionDescriptor connectionDescriptor,
                         StateHigherAuthority stateHigherAuthority,
                         SmppApiDelegator smppApiDelegator) {
        super(logger);
        this.smppApiDelegator = smppApiDelegator;
        this.setConnectionInformation(new ConnectionInformation(this, connectionGroupName, connectionDescriptor.getConnectionName()));
        stateHigherAuthority.addState(this.getConnectionInformation().getConnectionState());
        this.setIoReactorLock(new Object());
        this.setShutdown(false);
        this.connectionDescriptor = connectionDescriptor.getACopy();
        connectionDescriptor.setConnectionGroupName(connectionGroupName);
        this.setLabel(CommonUtils.getClientHostLabel(connectionGroupName, this.connectionDescriptor.getConnectionName()));

    }

    public int sendCancelSm(String messageId,
                            AddressDescriptor sourceAddress,
                            AddressDescriptor destinationAddress) throws Exception {

        Smpp34CancelSM cancelSM = new Smpp34CancelSM(this.getLogger());
        cancelSM.setParamMessageId(messageId);
        cancelSM.setSourceAddress(sourceAddress);
        cancelSM.setDestinationAddress(destinationAddress);

        SendBufferObject bufferObject = SmppBufferManager.getNextBufferObject();
        if (bufferObject == null) {
            throw new SmppApiException(SmppApiException.FATAL_ERROR, "No send buffer");
        }
        try {

            int sequenceNumber = this.getSequenceNumber();
            cancelSM.fillBody(bufferObject.getByteBuffer(), sequenceNumber);
            this.sendMsg(cancelSM, sequenceNumber, bufferObject.getByteBuffer());
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("Smpp34CancelSM",
                                       "send",
                                       0,
                                       null,
                                       " : -----> |-*Sending CANCEL SMS*-| SequenceID : " + sequenceNumber + " msgID : " + messageId);
            }
            return sequenceNumber;
        } catch (Exception e) {
            this.getLogger().error("SmppIOReactor", "sendCancelSm", 0, null, " : Error : " + e.getMessage(), e);
            throw e;
        } finally {
            SmppBufferManager.releaseBufferObject(bufferObject);
        }
    }


    public void initialize() throws SmppApiException {
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger()
                .debug("SmppIOReactor", "initialize", 0, this.getLabel(), "IO Reactor is initializing. --> " + this.connectionDescriptor.toString());
        }
        this.setResponseBuffer(new ResponseBufferImplementation(this.connectionDescriptor.getSmppWindowSize(), 2, this.getLogger(), 3000, this.getLabel()));
        if ((this.connectionDescriptor.getUsername().length() > 16) || (this.connectionDescriptor.getPassword().length() > 9)) {
            this.getLogger().error("SmppIOReactor", "initialize", 0, this.getLabel(), " : Error : Invalid input. Please check user and password length!!!");
            throw new SmppApiException(SmppApiException.PROTOCOL_ERROR, "invalid param length in userid, password or label params");
        }

        this.setSmppCallback(this.getConnectionDescriptor().getCallbackInterface());

        this.setConnectionController(new ConnectionController(this));
        this.getConnectionController().setDaemon(true);
        this.getConnectionController().start();


    }

    public void openConnectionAndSendLogin() throws Exception {
        this.openConnections();
        if (this.getConnectionInformation().isConnected()) {
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("SmppIOReactor", "openCon", 0, this.getLabel(), " IO Reactor initiated the connection.");
            }
            this.sendLoginMsg();
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("SmppIOReactor", "openCon", 0, this.getLabel(), "Binding user to host.");
            }
        }
    }

    public void openConnections() throws IOException {
        boolean done = false;
        int i = 0;
        while (!this.isShutdown() && !done && (i < 6)) {
            this.getConnectionInformation().setIp((this.getConnectionDescriptor().getIpList().get(this.order)));
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("SmppIOReactor",
                                       "openConnections",
                                       0,
                                       this.getLabel(),
                                       " : "
                                               + this.getConnectionDescriptor().getConnectionName()
                                               + " connection will be opened to Ip : "
                                               + this.getConnectionInformation().getIp()
                                               + " Port : "
                                               + this.getConnectionDescriptor().getPort()
                                               + " order : "
                                               + this.order
                                               + " try count "
                                               + (i + 1));
            }
            synchronized (this.getIoReactorLock()) {
                try {
                    this.setSocketChannel(SocketChannel.open());
                    this.getSocketChannel().connect(new InetSocketAddress(this.getConnectionInformation().getIp(), this.getConnectionDescriptor().getPort()));
                    this.getSocketChannel().socket().setKeepAlive(true);
                    this.getSocketChannel().configureBlocking(true);
                    if (!this.getSocketChannel().isConnected()) {
                        throw new IOException("Not connected");
                    }
                    done = true;
                    this.smppCallback.connected(this.getConnectionInformation());
                    this.getConnectionInformation().setConnected(true);
                    this.getConnectionInformation().setConnectionLabel("|"
                                                                       + this.getConnectionDescriptor().getConnectionGroupName()
                                                                       + "|"
                                                                       + this.getConnectionDescriptor().getConnectionName()
                                                                       + "|"
                                                                       + this.getConnectionInformation().getIp()
                                                                       + ":"
                                                                       + this.getConnectionDescriptor().getPort()
                                                                       + "|");
                    this.getConnectionInformation().getConnectionState().suspended();
                    break;
                } catch (IOException ioException) {
                    this.smppCallback.alarm(this.getConnectionInformation(), AlarmCode.IOError, ioException.getMessage());
                    this.getLogger().error("SmppIOReactor", "openConnections", 0, null, " : Error : " + ioException.getMessage(), ioException);
                    if (this.getConnectionDescriptor().getIpList().size() > 1) {
                        this.incConnectionOrder();
                    }
                    this.cleanupConnection(ioException.getMessage());
                }
            }

            i++;
            try {
                Thread.sleep(500L);
            } catch (Exception e) {
            }
        }
    }

    public void incConnectionOrder() {
        this.order++;
        this.order = this.order % this.getConnectionDescriptor().getIpList().size();
    }

    public void cleanupConnection(String reason) {
        try {
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
            this.getLogger().error("SmppIOReactor", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
        } finally {
            try {
                this.getConnectionInformation().setConnected(false);
                this.setSocketChannel(null);
                this.getConnectionInformation().getConnectionState().stopped();
                this.getResponseBuffer().resetBuffer();
                this.smppCallback.disconnected(this.getConnectionInformation());
            } catch (Exception e) {
                this.getLogger().error("SmppIOReactor", "closeConnection", 0, null, " : Error : " + e.getMessage(), e);
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
                    this.getLogger().info("SmppIOReactor", "closeConnection", 0, this.getLabel(), "#CLOSING CONNECTION# Description : " + description);
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
                        if (this.getResponseBuffer().getBufferBeans()[i].getWaitingObject().getMesssageType() == Smpp34Constants.MSG_SUBMIT_SM) {
                            SubmitSMMessage submitSM = (SubmitSMMessage) this.getResponseBuffer().getBufferBeans()[i].getWaitingObject();
                            submitSM.setSendResult(ESendResult.RETRY);
                            if (submitSM.getWaitObject() == null) {
                                this.smppCallback.submitResult(this.getConnectionInformation(), submitSM, submitSM.getAttachedObject());
                            } else {
                                synchronized (submitSM.getWaitObject()) {
                                    submitSM.getWaitObject().notify();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                this.getLogger().error("SmppIOReactor", "nackToWaitingObjects", 0, null, " : Error : " + e.getMessage(), e);
            }
            try {
                this.getResponseBuffer().getBufferBeans()[i].release();
            } catch (Exception e) {
                this.getLogger().error("SmppIOReactor", "nackToWaitingObjects", 0, null, " : Error : " + e.getMessage(), e);
            }
        }
    }

    private void sendLoginMsg() throws Exception {
        this.getBinded().set(false);
        this.getConnectionInformation().getConnectionState().suspended();
        try {
            this.bindTransceiver();
        } catch (Exception e) {
            this.incConnectionOrder();
            throw e;
        }
    }

    public void bindTransceiver() throws Exception {
        SendBufferObject nextBufferObject = SmppBufferManager.getNextBufferObject();
        if (nextBufferObject == null) {
            throw new SmppApiException(SmppApiException.FATAL_ERROR, "No send buffer");
        }
        try {
            ByteBuffer byteBuffer = nextBufferObject.getByteBuffer();
            BindRequestMessage bindRequestMessage = new BindRequestMessage();
            bindRequestMessage.setSystemIdentifier(this.getConnectionDescriptor().getUsername());
            bindRequestMessage.setPassword(this.getConnectionDescriptor().getPassword());
            bindRequestMessage.setConnectionType(this.getConnectionDescriptor().getConnectionType());
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug("SmppIOReactor",
                                       "bindTransceiver",
                                       0,
                                       null,
                                       " : Binding username : "
                                               + this.getConnectionDescriptor().getUsername()
                                               + " password : "
                                               + this.getConnectionDescriptor().getPassword()
                                               + " interfaceVersion : "
                                               + CommonUtils.INTERFACE_VERSION);
            }
            int sequenceNumber = SequenceGenerator.getNextSequenceNum();
            bindRequestMessage.fillBody(byteBuffer, sequenceNumber);
            this.writeBuffer(byteBuffer);
        } catch (Exception e) {
            this.getLogger().error("SmppIOReactor", "bindTransceiver", 0, null, " : Error : " + e.getMessage(), e);
        } finally {
            SmppBufferManager.releaseBufferObject(nextBufferObject);
        }
    }


    public void sendSubmitSMResponse(int seqno,
                                     ByteBuffer byteBuffer,
                                     int status) throws Exception {
        String msgRef = getNextAckNum() + "/" + System.currentTimeMillis();
        SubmitSMResponseMessage submitSMResponse = new SubmitSMResponseMessage();
        submitSMResponse.setMessageIdentifier(msgRef);
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


    public synchronized static long getNextAckNum() {
        ackNumber++;
        if (ackNumber > MAX_NUMBER) {
            ackNumber = MIN_NUMBER;
        }
        return ackNumber;
    }


    public SubmitSMMessage createSubmitSMMessage() {
        SubmitSMMessage smpp34SubmitSM = new SubmitSMMessage(this.getLogger(), TransactionManager.getNextTransactionID(), this.getLabel());
        return smpp34SubmitSM;
    }

    public SubmitSMMessage createSubmitSMMessage(long transactionId) {
        SubmitSMMessage smpp34SubmitSM = new SubmitSMMessage(this.getLogger(), transactionId, this.getLabel());
        return smpp34SubmitSM;
    }

    public void restartConnection() {
        this.closeConnection("Restart command received.");
    }

    public String getConnectionName() {
        return this.getConnectionDescriptor().getConnectionName();
    }

    public int getWindowSize() {
        return this.getResponseBuffer().getBufferSize();
    }

    public int getSmsType(int datacoding) {
        return 0;
    }

    public long sendSubmitSM(SubmitSMMessage smpp34SubmitSM,
                             boolean putBinaryHeader,
                             boolean isRequestDelivery,
                             Object returnObject) throws Exception {

        smpp34SubmitSM.init(putBinaryHeader, returnObject);

        int messageCount = smpp34SubmitSM.getMessageProcessor().getMessagePartCount();
        if (messageCount > 0) {
            for (int i = 0; i < messageCount; i++) {
                this.smppApiDelegator.getBlockingTpsCounter().increase();
                byte[] concatHeader = null;
                int sequenceNumber = this.getSequenceNumber();
                byte referenceNumber = this.getNextRefNumByte();
                if (putBinaryHeader) {
                    smpp34SubmitSM.setParamESMClass(64);
                    concatHeader = smpp34SubmitSM.getConcatHeader(messageCount, i + 1, referenceNumber);
                } else {
                    smpp34SubmitSM.setOpParamSarMsgRefNum(referenceNumber);
                    smpp34SubmitSM.setOpParamSarSegmentSequenceNum(i + 1);
                    smpp34SubmitSM.setOpParamSarTotalSegments(messageCount);
                }
                SendBufferObject nextBufferObject = SmppBufferManager.getNextBufferObject();
                if (nextBufferObject == null) {
                    throw new SmppApiException(SmppApiException.FATAL_ERROR, "No send buffer");
                }
                try {
                    if (isRequestDelivery) {
                        if (i == (messageCount - 1)) {
                            smpp34SubmitSM.setParamRegisteredDelivery(1);
                        } else {
                            smpp34SubmitSM.setParamRegisteredDelivery(0);
                        }
                    } else {
                        smpp34SubmitSM.setParamRegisteredDelivery(0);
                    }
                    smpp34SubmitSM.getMessageProcessor().addSequence(i, sequenceNumber);
                    smpp34SubmitSM.fillBuffer(nextBufferObject.getByteBuffer(), sequenceNumber, concatHeader, i);
                    this.sendMsg(smpp34SubmitSM, sequenceNumber, nextBufferObject.getByteBuffer());
                } finally {
                    SmppBufferManager.releaseBufferObject(nextBufferObject);
                }
            }
        }
        return smpp34SubmitSM.getTransID();
    }

    @Override
    public boolean handleCloseConnection() {
        if (!this.isShutdown()) {
            try {
                Thread.sleep(1000L);
            } catch (final InterruptedException e) {
            }
            try {
                this.openConnectionAndSendLogin();
            } catch (final Exception ex) {
                this.getLogger().error("ConnectionController", "run", 0, null, " : Error : " + ex.getMessage(), ex);
                this.closeConnection(ex.getMessage());
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void handleTimeoutRequests() {
        TimeoutConsumerThread timeoutConsumerThread = new TimeoutConsumerThread(this, this.getResponseBuffer().getTimeoutQueue());
        timeoutConsumerThread.start();

    }


    public SmppConnectionType getConnectionType() {
        return this.getConnectionDescriptor().getConnectionType();
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

    public ISmppCallback getSmppCallback() {
        return this.smppCallback;
    }

    public void setSmppCallback(ISmppCallback smppCallback) {
        this.smppCallback = smppCallback;
    }


    @Override
    public ISmppMessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    public ConnectionDescriptor getConnectionDescriptor() {
        return this.connectionDescriptor;
    }

    public void setConnectionDescriptor(ConnectionDescriptor connectionDescriptor) {
        this.connectionDescriptor = connectionDescriptor;
    }


    @Override
    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void updateConnectionDescriptor(ConnectionDescriptor connectionDescriptor) {
        this.connectionDescriptor = connectionDescriptor.getACopy();
    }


    @Override
    public int getMaxThreadCount() {
        return this.connectionDescriptor.getMaxThreadCount();
    }

    @Override
    public boolean isTraceON() {
        return this.connectionDescriptor.isTraceON();
    }

    @Override
    public void handleNack(BufferBean bufferBean,
                           MessageHeader smpp34Header) {
        if (bufferBean.getWaitingObject().getMesssageType() == Smpp34Constants.MSG_SUBMIT_SM) {
            if (bufferBean.getWaitingObject() != null) {
                final SubmitSMMessage sentSubmitSM = (SubmitSMMessage) bufferBean.getWaitingObject();
                sentSubmitSM.setSendResult(ESendResult.FATAL_ERROR);
                sentSubmitSM.getMessageProcessor().errorReceived();
                if (sentSubmitSM.getWaitObject() == null) {
                    this.getSmppCallback().submitResult(this.getConnectionInformation(), sentSubmitSM, sentSubmitSM.getAttachedObject());
                } else {
                    synchronized (sentSubmitSM.getWaitObject()) {
                        sentSubmitSM.getWaitObject().notify();
                    }
                }
            }
        } else if (bufferBean.getWaitingObject().getMesssageType() == Smpp34Constants.MSG_CANCEL_SM) {
            if (bufferBean.getWaitingObject() != null) {
                final Smpp34CancelSM smpp34CancelSM = (Smpp34CancelSM) bufferBean.getWaitingObject();
                this.getSmppCallback().cancelResult(this.getConnectionInformation(),
                                                    smpp34Header.getSequenceNo(),
                                                    smpp34Header.getCommandStatus(),
                                                    smpp34CancelSM.getParamMessageId());
            }
        }

    }

    @Override
    public boolean isBinded() {
        return this.getBinded().get();
    }

}
