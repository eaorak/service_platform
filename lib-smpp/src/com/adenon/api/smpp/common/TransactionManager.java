package com.adenon.api.smpp.common;

public class TransactionManager {

    private static int          MAX            = Integer.MAX_VALUE;
    private static int          transId        = 1;

    private static final Object nextLockObject = new Object();

    public static int getNextTransactionID() {
        synchronized (nextLockObject) {
            transId++;
            if (transId >= MAX) {
                transId = 1;
            }
            return transId;
        }
    }
}
