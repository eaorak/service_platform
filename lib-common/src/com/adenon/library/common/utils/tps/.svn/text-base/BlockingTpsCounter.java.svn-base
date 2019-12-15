package com.adenon.library.common.utils.tps;


public class BlockingTpsCounter {

    private int  maxTps;
    private int  counter;
    private long startTime;

    public BlockingTpsCounter(int maxTps) {
        this.setMaxTps(maxTps);
        this.startTime = System.currentTimeMillis();
    }

    public synchronized int increase() {
        this.counter++;
        if (this.counter > this.getMaxTps()) {
            long delta = (System.currentTimeMillis() - this.startTime);
            if (delta < 1000) {
                try {
                    Thread.sleep(1000 - delta);
                } catch (Exception e) {
                }
            }
            this.startTime = System.currentTimeMillis();
            this.counter = 1;
        }
        return this.counter;
    }


    public int getMaxTps() {
        return this.maxTps;
    }

    public void setMaxTps(int maxTps) {
        this.maxTps = maxTps;
    }

    public static void main(String[] args) {
        final BlockingTpsCounter counter = new BlockingTpsCounter(100);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            new Thread() {

                private int myId = index;

                @Override
                public void run() {
                    while (true) {
                        int lastCount = counter.increase();
                        System.out.println("Id: " + this.myId + " Time: " + System.currentTimeMillis() + " Count : " + lastCount);
                    }
                }
            }.start();
        }
    }

}
