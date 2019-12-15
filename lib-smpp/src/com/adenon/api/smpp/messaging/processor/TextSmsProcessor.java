package com.adenon.api.smpp.messaging.processor;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.adenon.api.smpp.common.CommonParameters;
import com.adenon.api.smpp.common.CommonUtils;
import com.adenon.api.smpp.common.SmppApiException;
import com.adenon.api.smpp.core.ResponseHandler;
import com.adenon.api.smpp.logging.LoggerWrapper;


public class TextSmsProcessor extends ResponseHandler {

    private String                                 smsText;
    private final LoggerWrapper                    logger;
    private final String                           label;
    private final long                             transID;
    private final char[]                           smsTextChars;
    private final ArrayList<MessagePartDescriptor> concatMessagePointer = new ArrayList<MessagePartDescriptor>(10);
    private final int                              messageEncodingType;
    private boolean                                isConcatMsg          = false;

    public TextSmsProcessor(String text,
                            int encodingType,
                            LoggerWrapper pLogger,
                            long pTransId,
                            String pLabel) throws SmppApiException {
        this.transID = pTransId;
        this.label = pLabel;
        this.logger = pLogger;
        this.smsText = text;
        this.messageEncodingType = encodingType;

        if (this.smsText == null) {
            this.smsText = "";
        }
        this.smsTextChars = this.smsText.toCharArray();

        final int encodingCode = (byte) ((byte) encodingType & (byte) 0x0f);
        // Calculate msg parts
        final int msgMaxByteCount = CommonParameters.BYTE_COUNT_FOR_SMS;
        double multiplier = 0.5;
        if (encodingCode == CommonParameters.DATA_CODING_UCS2) {
            multiplier = 0.5;
        } else if (encodingCode == CommonParameters.DATA_CODING_BINARY) {
            multiplier = 2;
            if ((this.smsText.length() % 2) == 1) {
                throw new SmppApiException(SmppApiException.FATAL_ERROR, SmppApiException.DOMAIN_IOREACTOR, "SMS Byte count is not odd : "
                                                                                                            + this.smsText.length());
            }
        } else {
            multiplier = (double) 8 / (double) 7;
        }

        double remainBytes;
        int charactersLeft;
        int messageCounter = 0;
        int reservedBytes;
        int previousIndex = 0;
        int msgRemainLength = this.smsText.length();
        while (msgRemainLength > 0) {
            reservedBytes = 0;
            if (this.isConcatMsg) {
                reservedBytes += 5;
            } else {
                final double actualLength = (msgRemainLength / multiplier);
                int actualBytes = (int) actualLength;
                if (actualLength > actualBytes) {
                    actualBytes++;
                }
                if ((reservedBytes + actualBytes) > msgMaxByteCount) {
                    this.isConcatMsg = true;
                    reservedBytes += 5;
                }
            }
            if (reservedBytes > 0) {
                reservedBytes++;
            }
            remainBytes = msgMaxByteCount - reservedBytes;
            charactersLeft = (int) (remainBytes * multiplier);
            if (encodingCode == CommonParameters.DATA_CODING_BINARY) {
                if ((charactersLeft % 2) == 1) {
                    charactersLeft--;
                }
            }
            if (charactersLeft >= msgRemainLength) {
                final MessagePartDescriptor messagePartDescriptor = new MessagePartDescriptor();
                messagePartDescriptor.setStart(previousIndex);
                messagePartDescriptor.setEnd(previousIndex + msgRemainLength);
                messagePartDescriptor.setLength(msgRemainLength);
                this.concatMessagePointer.add(messagePartDescriptor);
                msgRemainLength = 0;
                previousIndex += msgRemainLength;
            } else {
                final MessagePartDescriptor messagePartDescriptor = new MessagePartDescriptor();
                messagePartDescriptor.setStart(previousIndex);
                messagePartDescriptor.setEnd(previousIndex + charactersLeft);
                messagePartDescriptor.setLength(charactersLeft);
                this.concatMessagePointer.add(messagePartDescriptor);
                msgRemainLength -= charactersLeft;
                previousIndex += charactersLeft;
            }
            messageCounter++;
        }
        this.createHandler(messageCounter);
    }

    @Override
    public void fillMessageBody(final ByteBuffer buffer,
                                final int index,
                                final byte[] concatHeader) throws Exception {
        int headerLength = 0;
        if (concatHeader != null) {
            headerLength += concatHeader.length;
        }
        int smsLength = 0;

        if (this.concatMessagePointer.size() == 0) {
            buffer.put((byte) 0);
            return;
        }

        final MessagePartDescriptor messagePartDescriptor = this.concatMessagePointer.get(index);

        if (this.messageEncodingType == CommonParameters.DATA_CODING_UCS2) {
            smsLength = (messagePartDescriptor.getLength() * 2);
        } else if (this.messageEncodingType == CommonParameters.DATA_CODING_BINARY) {
            smsLength = (messagePartDescriptor.getLength() / 2);
        } else {
            smsLength = messagePartDescriptor.getLength();
        }
        smsLength += headerLength;
        buffer.put((byte) smsLength);
        if (headerLength > 0) {
            buffer.put((byte) headerLength);
            if (concatHeader != null) {
                buffer.put(concatHeader);
            }
        }
        if (this.messageEncodingType == CommonParameters.DATA_CODING_UCS2) {
            for (int i = messagePartDescriptor.getStart(); i < messagePartDescriptor.getEnd(); i++) {
                try {
                    buffer.putChar(this.smsTextChars[i]);
                } catch (final Exception e) {
                    this.logger.error("TextSmsProcessor", "fillBody", this.transID, this.label, " : Error : " + e.getMessage(), e);
                }
            }
        } else if (this.messageEncodingType == CommonParameters.DATA_CODING_BINARY) {
            try {
                final byte[] retVal = CommonUtils.strToByteAll(this.smsText, messagePartDescriptor.getStart(), messagePartDescriptor.getLength());
                buffer.put(retVal);
            } catch (final Exception e) {
                this.logger.error("TextSmsProcessor", "fillBody", this.transID, this.label, " : Error : " + e.getMessage(), e);
            }

        } else {
            for (int i = messagePartDescriptor.getStart(); i < messagePartDescriptor.getEnd(); i++) {
                try {
                    buffer.put((byte) this.smsTextChars[i]);
                } catch (final Exception e) {
                    this.logger.error("TextSmsProcessor", "fillBody", this.transID, this.label, " : Error : " + e.getMessage(), e);
                }
            }
        }
    }
}
