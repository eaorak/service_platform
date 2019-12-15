package com.adenon.library.common.utils.tps;


public class NonBlockingTpsCounter {

    private int  maxTps;
    private int  counter;
    private long startTime;

    public NonBlockingTpsCounter(int maxTps) {
        this.maxTps = maxTps;
        this.startTime = System.currentTimeMillis();
    }

    public synchronized int increase() {
        this.counter++;
        if (this.counter > this.maxTps) {
            long delta = (System.currentTimeMillis() - this.startTime);
            if (delta < 1000) {
                return -1;
            } else {
                this.startTime = System.currentTimeMillis();
                this.counter = 1;
                return this.counter;
            }
        } else {
            return this.counter;
        }
    }

    public static void main(String[] args) {
        final NonBlockingTpsCounter counter = new NonBlockingTpsCounter(100);
        for (int i = 0; i < 30; i++) {
            final int index = i;
            new Thread() {

                private int myId = index;

                @Override
                public void run() {
                    while (true) {
                        int increase = counter.increase();
                        if (increase > -1) {
                            System.out.println("Id: " + this.myId + " Time: " + System.currentTimeMillis() + " counter : " + counter.increase());
                        }
                    }
                }
            }.start();
        }
    }
}
