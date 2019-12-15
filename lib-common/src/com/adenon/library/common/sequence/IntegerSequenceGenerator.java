package com.adenon.library.common.sequence;


public class IntegerSequenceGenerator {

    public static final int     MAXIMUM_SEQUENCE_NUMBER = Integer.MAX_VALUE - 1;
    public static final int     MINIMUM_SEQUENCE_NUMBER = 100;


    private int                 sequenceNumber          = MINIMUM_SEQUENCE_NUMBER;

    private static final Object lock                    = new Object();

    public IntegerSequenceGenerator() {

    }

    public int getNextIntegerSequenceNum() {
        synchronized (lock) {
            this.sequenceNumber++;
            if (this.sequenceNumber > MAXIMUM_SEQUENCE_NUMBER) {
                this.sequenceNumber = MINIMUM_SEQUENCE_NUMBER;
            }
            return this.sequenceNumber;
        }
    }

}
