package com.adenon.sp.executer.config;

import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.executer.pool.TaskPriority;


@MBean(parent = PoolConfiguration.class, id = "bundleName")
public class ProxyConfiguration {

    private PoolConfiguration poolConf;
    private String            bundleName;
    private int               priority    = TaskPriority.NORMAL.asNumber();
    private int               sampleCount = 50;
    private String            poolName;

    public ProxyConfiguration() {
    }

    public ProxyConfiguration(String bundleName,
                              String poolName) {
        this.bundleName = bundleName;
        this.poolName = poolName;
    }

    @Join
    public String getPoolName() {
        return this.poolName;
    }

    // @Attribute(readOnly = true)
    public String getBundleName() {
        return this.bundleName;
    }

    @Deprecated
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    // @Attribute
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // @Attribute
    public int getSampleCount() {
        return this.sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public PoolConfiguration getPoolConf() {
        return this.poolConf;
    }

    public void setPoolConf(PoolConfiguration poolConf) {
        this.poolConf = poolConf;
    }

}
