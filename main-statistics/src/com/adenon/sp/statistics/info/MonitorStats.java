package com.adenon.sp.statistics.info;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.adenon.sp.monitoring.counters.ISnapshot;

public class MonitorStats {

    private static final long     DELTA_PER_MINUTE = 250;
    private final String          monitorName;
    private final TimeUnit        interval;
    private final long            duration;
    private long                  snapshotTime;
    private final List<ISnapshot> snapshots        = new ArrayList<ISnapshot>();

    public MonitorStats(String monitorName,
                        TimeUnit interval,
                        long duration) {
        if (interval.ordinal() < TimeUnit.MINUTES.ordinal()) {
            throw new RuntimeException("Interval can not be lower than " + TimeUnit.MINUTES + " !");
        }
        this.setSnapshotTime();
        long acceptableDelta = interval.toMinutes(duration) * DELTA_PER_MINUTE;
        this.duration = interval.toMillis(duration) - acceptableDelta;
        this.monitorName = monitorName;
        this.interval = interval;
    }

    public String getMonitorName() {
        return this.monitorName;
    }

    public TimeUnit getInterval() {
        return this.interval;
    }

    public long getSnapshotTime() {
        return this.snapshotTime;
    }

    public void setSnapshotTime() {
        this.snapshotTime = System.currentTimeMillis();
    }

    public boolean takeSnapshot() {
        return (this.snapshotTime + this.duration) <= System.currentTimeMillis();
    }

    public void addSnapshot(ISnapshot snapshot) {
        this.snapshots.add(snapshot);
    }

    public void saveSnapshots(EntityManager em) {
        EntityTransaction tx = null;
        boolean success = false;
        try {
            tx = em.getTransaction();
            tx.begin();
            for (ISnapshot s : this.snapshots) {
                Statistics statistics = new Statistics(s);
                em.persist(statistics);
            }
            success = true;
        } finally {
            if (success) {
                tx.commit();
                this.snapshots.clear();
            } else {
                tx.rollback();
            }
        }
    }

    @Override
    public String toString() {
        return "MonitorStats [monitorName="
               + this.monitorName
               + ", interval="
               + this.interval
               + ", duration="
               + this.duration
               + ", snapshotTime="
               + this.snapshotTime
               + "]";
    }

}
