package com.adenon.sp.statistics.info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.adenon.sp.monitoring.counters.ISnapshot;

@Entity
public class Statistics {

    @Id
    @GeneratedValue
    private long   recordId;
    private String monitor;
    private String counterKey;
    private long   counterValue;
    private long   recordTime;

    public Statistics() {
    }

    public Statistics(ISnapshot snapshot) {
        this.monitor = snapshot.getMonitorName();
        this.counterKey = snapshot.getKey();
        this.counterValue = snapshot.getValue();
        this.recordTime = snapshot.getTime();
    }

    public long getRecordId() {
        return this.recordId;
    }


    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getMonitor() {
        return this.monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getCounterKey() {
        return this.counterKey;
    }


    public void setCounterKey(String counterKey) {
        this.counterKey = counterKey;
    }


    public long getCounterValue() {
        return this.counterValue;
    }


    public void setCounterValue(long counterValue) {
        this.counterValue = counterValue;
    }


    public long getRecordTime() {
        return this.recordTime;
    }


    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "Statistics [recordId="
               + this.recordId
               + ", monitor="
               + this.monitor
               + ", counterKey="
               + this.counterKey
               + ", counterValue="
               + this.counterValue
               + ", recordTime="
               + this.recordTime
               + "]";
    }

}
