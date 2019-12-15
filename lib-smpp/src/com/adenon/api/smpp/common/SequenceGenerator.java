package com.adenon.api.smpp.common;

public class SequenceGenerator {

    public static final int     MAX_SEQUENCE_NO     = 2147483640;
    public static final int     MIN_SEQUENCE_NO     = 100;

    public static final short   MAX_MSG_REF         = 254;
    public static final short   MIN_MSG_REF         = 1;

    public static final byte    MAX_MSG_REF_BYTE    = (byte) 0xFF;
    public static final byte    MIN_MSG_REF_BYTE    = (byte) 0x01;

    private static int          sequenceNumber      = MIN_SEQUENCE_NO;
    private static short        referenceNumber     = MIN_MSG_REF;
    private static byte         referenceNumberByte = MIN_MSG_REF_BYTE;

    private static final Object lockSequence        = new Object();
    private static final Object lockRefNum          = new Object();
    private static final Object lockRefNumByte      = new Object();

    private SequenceGenerator() {

    }

    public static int getNextSequenceNum() {
        synchronized (lockSequence) {
            sequenceNumber++;
            if (sequenceNumber > MAX_SEQUENCE_NO) {
                sequenceNumber = MIN_SEQUENCE_NO;
            }
            return sequenceNumber;
        }
    }

    public static short getNextRefNum() {
        synchronized (lockRefNum) {
            referenceNumber++;
            if (referenceNumber > MAX_MSG_REF) {
                referenceNumber = MIN_MSG_REF;
            }
            return referenceNumber;
        }
    }

    public static byte getNextRefNumByte() {
        synchronized (lockRefNumByte) {
            referenceNumberByte++;
            if (referenceNumberByte > MAX_MSG_REF_BYTE) {
                referenceNumberByte = MIN_MSG_REF_BYTE;
            }
            return referenceNumberByte;
        }
    }

}
