package com.adenon.api.smpp.message;

import java.nio.ByteBuffer;

import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.ParamInterface;
import com.adenon.api.smpp.common.Smpp34Constants;
import com.adenon.api.smpp.logging.LoggerWrapper;
import com.adenon.api.smpp.messaging.processor.IMessageProcessor;
import com.adenon.api.smpp.messaging.processor.TextSmsProcessor;
import com.adenon.api.smpp.messaging.processor.WapPushProcessor;
import com.adenon.api.smpp.sdk.ESendResult;
import com.adenon.api.smpp.sdk.IMessage;
import com.adenon.api.smpp.sdk.TextMessageDescriptor;

public class SubmitSMMessage extends OptionalParameters implements MessageObject {

    private final LoggerWrapper logger;

    private final long          transactionId;

    private final String        label;

    private IMessageProcessor   messageProcessor;

    private Object              attachedObject;

    private Object              waitObject;

    private IMessage            message;

    private ESendResult         sendResult;

    private int                 serverErrorCode;


    public SubmitSMMessage(LoggerWrapper logger,
                           long transactionId,
                           String label) {
        super(logger, Smpp34Constants.MSG_SUBMIT_SM);
        this.logger = logger;
        this.transactionId = transactionId;
        this.label = label;
    }

    public void parseMessage(final ByteBuffer byteBuffer) throws Exception {
        super.parseMandatoryParameters(byteBuffer, this.transactionId, this.label);

        super.parseOpitionalParameters(byteBuffer, this.transactionId, this.label);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("SubmitSMMessage", "parseMessage", 0, this.label, "Submit SM Received : " + this.toString());
        }
    }


    public void init(final boolean willPutConcatHeader,
                     final Object attachedObject) throws Exception {
        this.setAttachedObject(attachedObject);
        switch (this.message.getMessageType()) {
            case SMSText:
                TextMessageDescriptor messageDescriptor = (TextMessageDescriptor) this.message;
                this.setParamDataCoding(messageDescriptor.getDataCoding());
                this.setParamShortMessage(messageDescriptor.getMessage());
                this.messageProcessor = new TextSmsProcessor(this.getParamShortMessage(), this.getParamDataCoding(), this.logger, this.getTransID(), this.label);
                break;

            case WAPPushSI:
            case WAPPushSL:
            case WAPBookmark:
                this.setPutConcatOptionalParameters(false);
                this.setParamESMClass(64);
                this.setParamDataCoding(4);
                this.messageProcessor = new WapPushProcessor(this.message);
                break;

            default:
                break;
        }
    }


    public void fillBuffer(final ByteBuffer byteBuffer,
                           final int sequenceNumber,
                           final byte[] concatHeader,
                           final int messageIndex) throws Exception {
        super.fillMandatoryParameters(byteBuffer, sequenceNumber);
        this.messageProcessor.fillMessageBody(byteBuffer, messageIndex, concatHeader);
        this.fillOptionalParameters(byteBuffer);
        CommonUtils.setLength(byteBuffer);
    }


    public byte[] getConcatHeader(int maxSMS,
                                  int currentSMS,
                                  int ref) {
        final byte[] totBytes = new byte[5];
        totBytes[0] = 0;
        totBytes[1] = 3;
        totBytes[2] = (byte) ref;
        totBytes[3] = (byte) maxSMS;
        totBytes[4] = (byte) currentSMS;
        return totBytes;
    }

    public int[] expandArray(final int[] oldArray,
                             final int size) {
        final int[] retArray = new int[size];
        System.arraycopy(oldArray, 0, retArray, 0, oldArray.length);
        return retArray;
    }


    public static byte[] convertUnicodeStrToByte(final String _str) throws Exception {
        final byte[] temp = _str.getBytes("UnicodeBig");
        final byte[] retbytes = new byte[(temp.length / 2) - 1];
        for (int i = 0; i < retbytes.length; i++) {
            retbytes[i] = temp[(i * 2) + 3];
        }
        return retbytes;
    }

    @Override
    public int getMesssageType() {
        return Smpp34Constants.MSG_SUBMIT_SM;
    }

    public String getMsg() {
        return this.getParamShortMessage();
    }

    public String getDeliveryMsgId() {
        return null;
    }

    public int getMsgPartCount() {
        return this.messageProcessor.getMessagePartCount();
    }

    public int getMsgPartIndex() {
        if (this.getOpParamSarSegmentSequenceNum() > 0) {
            return this.getOpParamSarSegmentSequenceNum();
        } else {
            return 1;
        }
    }

    public int getMsgPartRef() {
        return this.getOpParamSarMsgRefNum();
    }

    public void setParam(final ParamInterface paramInterface) {
        switch (paramInterface.getParamId()) {
            case Smpp34Constants.PAR_SERVICE_TYPE:
                this.setParamServiceType(paramInterface.getStringVal());
                break;
            case Smpp34Constants.PAR_ESM_CLASS:
                this.setParamESMClass(paramInterface.getIntegerVal());
                break;
            case Smpp34Constants.PAR_PROTOCOL_ID:
                this.setParamProtocolID(paramInterface.getIntegerVal());
                break;
            case Smpp34Constants.PAR_PRIORITY_FLAG:
                this.setParamPriorityFlag(paramInterface.getIntegerVal());
                break;
            case Smpp34Constants.PAR_SCHED_DLVRY_TIME:
                this.setParamScheduleDeliveryTime(paramInterface.getStringVal());
                break;
            case Smpp34Constants.PAR_DLVRY_FLAG:
                this.setParamRegisteredDelivery(paramInterface.getIntegerVal());
                break;
            case Smpp34Constants.PAR_REPLACE_PRESENT:
                this.setParamReplaceIfPresentFlag(paramInterface.getIntegerVal());
                break;
            case Smpp34Constants.PAR_SM_DEFAULT:
                this.setParamSMDefaultMsgID(paramInterface.getIntegerVal());
                break;
            default:
                break;
        }
    }

    public void setOptParam(final ParamInterface paramInterface) {
        switch (paramInterface.getParamId()) {
            case Smpp34Constants.OPT_USSD_SERVICE_OP:
                this.setOpParamUSSDServiceOp(paramInterface.getIntegerVal());
                break;
            default:
                break;
        }
    }


    @Override
    public String getDescription() {
        return this.toString();
    }

    @Override
    public String toString() {
        return " *SUBMIT SM* " + super.toString();
    }


    public long getTransID() {
        return this.transactionId;
    }

    public IMessageProcessor getMessageProcessor() {
        return this.messageProcessor;
    }

    public void setMessageProcessor(IMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public Object getAttachedObject() {
        return this.attachedObject;
    }

    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    public IMessage getMessage() {
        return this.message;
    }


    public void setMessage(IMessage message) {
        this.message = message;
    }

    public Object getWaitObject() {
        return this.waitObject;
    }

    public void setWaitObject(Object waitObject) {
        this.waitObject = waitObject;
    }

    public ESendResult getSendResult() {
        return this.sendResult;
    }

    public void setSendResult(ESendResult sendResult) {
        this.sendResult = sendResult;
    }

    public String getMessageIdentifier() {
        return this.messageProcessor.getMessageIdentifier();
    }

    public int getServerErrorCode() {
        return this.serverErrorCode;
    }

    public void setServerErrorCode(int serverErrorCode) {
        this.serverErrorCode = serverErrorCode;
    }


}
