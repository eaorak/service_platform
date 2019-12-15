package com.adenon.sp.statistics.execute;


import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.adenon.sp.statistics.info.MonitorStats;


public class PersistTask implements Runnable {

    private static final Logger logger = Logger.getLogger(PersistTask.class);
    private final EntityManager entityManager;
    private final MonitorStats  stats;

    public PersistTask(EntityManager entityManager,
                       MonitorStats stats) {
        this.entityManager = entityManager;
        this.stats = stats;
    }

    @Override
    public void run() {
        try {
            this.stats.saveSnapshots(this.entityManager);
        } catch (Exception e) {
            logger.error("[PersistTask][run] : Error while saving stats [" + this.stats + "] " + e.getMessage(), e);
        }
    }

}
