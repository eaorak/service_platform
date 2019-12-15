package com.adenon.sp.channel.smsserver.beans;

import java.util.concurrent.atomic.AtomicLong;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.administration.annotate.Operation;


@MBean(parent = UsersBean.class, persist = true, id = "username")
public class UserBean {

    private String     username;
    private String     password;
    private int        maxConnection               = 1;
    private int        currentConnectionCount      = 0;
    private boolean    connected                   = false;

    private AtomicLong counterSuccessfullDelivery  = new AtomicLong(0);
    private AtomicLong counterRetryDelivery        = new AtomicLong(0);
    private AtomicLong counterFailedDelivery       = new AtomicLong(0);

    private AtomicLong counterSuccessfullDeliverSM = new AtomicLong(0);
    private AtomicLong counterRetryDeliverSM       = new AtomicLong(0);
    private AtomicLong counterFailedDeliverSM      = new AtomicLong(0);

    private AtomicLong counterSubmitSM             = new AtomicLong(0);

    private AtomicLong counterResultSuccess        = new AtomicLong(0);
    private AtomicLong counterResultFailed         = new AtomicLong(0);
    private AtomicLong counterResultRetry          = new AtomicLong(0);

    @Join
    @Attribute
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Attribute
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Attribute
    public int getMaxConnection() {
        return this.maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    @Attribute
    public int getCurrentConnectionCount() {
        return this.currentConnectionCount;
    }

    public void setCurrentConnectionCount(int currentConnectionCount) {
        this.currentConnectionCount = currentConnectionCount;
    }

    public boolean accept(String username,
                          String password) {
        if (username.equals(this.getUsername()) && password.endsWith(this.getPassword()) && ((this.currentConnectionCount + 1) <= this.maxConnection)) {
            return true;
        }
        return false;
    }

    @Attribute
    public boolean getConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Attribute(readOnly = true)
    public long getCounterSuccessfullDelivery() {
        return this.counterSuccessfullDelivery.get();
    }


    public void setCounterSuccessfullDelivery(long counterSuccessfullDelivery) {
    }


    @Attribute(readOnly = true)
    public long getCounterRetryDelivery() {
        return this.counterRetryDelivery.get();
    }


    public void setCounterRetryDelivery(long counterRetryDelivery) {
    }


    @Attribute(readOnly = true)
    public long getCounterFailedDelivery() {
        return this.counterFailedDelivery.get();
    }


    public void setCounterFailedDelivery(long counterFailedDelivery) {
    }


    @Attribute(readOnly = true)
    public long getCounterSuccessfullDeliverSM() {
        return this.counterSuccessfullDeliverSM.get();
    }


    public void setCounterSuccessfullDeliverSM(long counterSuccessfullDelivery) {
    }


    @Attribute(readOnly = true)
    public long getCounterRetryDeliverSM() {
        return this.counterRetryDeliverSM.get();
    }


    public void setCounterRetryDeliverSM(long counterRetryDelivery) {
    }


    @Attribute(readOnly = true)
    public long getCounterFailedDeliverSM() {
        return this.counterFailedDeliverSM.get();
    }


    public void setCounterFailedDeliverSM(long counterFailedDelivery) {
    }

    public void increaseCounterSuccessfullDelivery() {
        this.counterSuccessfullDelivery.incrementAndGet();
    }

    public void increaseCounterRetryDelivery() {
        this.counterRetryDelivery.incrementAndGet();

    }

    public void increaseCounterFailedDelivery() {
        this.counterFailedDelivery.incrementAndGet();

    }

    public void increaseCounterSuccessfullDeliverSM() {
        this.counterSuccessfullDeliverSM.incrementAndGet();
    }

    public void increaseCounterRetryDeliverSM() {
        this.counterRetryDeliverSM.incrementAndGet();

    }

    public void increaseCounterFailedDeliverSM() {
        this.counterFailedDeliverSM.incrementAndGet();

    }

    public void increaseCounterSubmitSM() {
        this.counterSubmitSM.incrementAndGet();

    }

    @Operation
    public String resetCounters() throws Exception {
        this.counterSuccessfullDelivery.set(0);
        this.counterRetryDelivery.set(0);
        this.counterFailedDelivery.set(0);
        this.counterSuccessfullDelivery.set(0);
        this.counterRetryDelivery.set(0);
        this.counterFailedDelivery.set(0);
        this.counterSubmitSM.set(0);
        this.counterResultSuccess.set(0);
        this.counterResultFailed.set(0);
        this.counterResultRetry.set(0);
        return "OK";
    }

    @Attribute(readOnly = true)
    public long getCounterSubmitSM() {
        return this.counterSubmitSM.get();
    }

    public void setCounterSubmitSM(long counterSubmitSM) {
    }

    @Attribute(readOnly = true)
    public long getCounterResultSuccess() {
        return this.counterResultSuccess.get();
    }

    public void setCounterResultSuccess(long counterResultSuccess) {
    }

    @Attribute(readOnly = true)
    public long getCounterResultFailed() {
        return this.counterResultFailed.get();
    }

    public void setCounterResultFailed(long counterResultFailed) {
    }

    @Attribute(readOnly = true)
    public long getCounterResultRetry() {
        return this.counterResultRetry.get();
    }

    public void setCounterResultRetry(long counterResultRetry) {
    }

    public void increaseDeliverySuccess() {
        this.counterResultSuccess.incrementAndGet();

    }

    public void increaseDeliveryFailed() {
        this.counterResultFailed.incrementAndGet();

    }

    public void increaseDeliveryRetry() {
        this.counterResultRetry.incrementAndGet();

    }
}
