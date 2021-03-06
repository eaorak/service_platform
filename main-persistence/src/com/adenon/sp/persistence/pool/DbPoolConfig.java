package com.adenon.sp.persistence.pool;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.kernel.utils.Properties;
import com.adenon.sp.persistence.DbConfig;
import com.adenon.sp.persistence.IPoolConfig;
import com.adenon.sp.persistence.PersistenceBean;
import com.adenon.sp.persistence.SystemPool;
import com.jolbox.bonecp.BoneCPConfig;


@MBean(parent = PersistenceBean.class, id = "name")
public class DbPoolConfig implements IPoolConfig {

    private String       name;
    private String       url;
    private String       user;
    private String       pass;
    private String       driver;
    private int          minSize = 5;
    private int          maxSize = 10;
    private BoneCPConfig config;
    private boolean      useJpa;

    public static DbPoolConfig createConfig(SystemPool sysPool,
                                            Properties p) throws Exception {
        Properties props = p.forParent(sysPool);
        String url = props.get(DbConfig.URL).value();
        String user = props.get(DbConfig.USER).value();
        String pass = props.get(DbConfig.PASS).value();
        String driver = props.get(DbConfig.DRIVER).value();
        int minConn = props.get(DbConfig.INIT_CONNECTION).toInt();
        int maxConn = props.get(DbConfig.MAX_CONNECTION).toInt();
        //
        DbPoolConfig config = new DbPoolConfig(sysPool.key(), url);
        config.setCredentials(user, pass);
        config.setDriver(driver);
        config.setMinSize(minConn);
        config.setMaxSize(maxConn);
        return config;
    }


    public DbPoolConfig() {
    }

    public DbPoolConfig(String name,
                        String url) {
        if ((name == null) || (url == null)) {
            throw new RuntimeException("Name or url can not be null !");
        }
        this.name = name;
        this.url = url;
    }

    @Override
    public IPoolConfig setCredentials(String user,
                                      String pass) {
        this.user = user;
        this.pass = pass;
        return this;
    }

    @Override
    @Attribute(readOnly = true)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    @Attribute
    public String getDriver() {
        return this.driver;
    }

    @Override
    public IPoolConfig setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    @Override
    @Attribute(readOnly = true)
    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    @Attribute(readOnly = true)
    @Join
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Attribute(readOnly = true)
    public String getPass() {
        return "*************";
    }

    public String getRealPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    @Attribute(name = "Minimum size", readOnly = true)
    public int getMinSize() {
        return this.minSize;
    }

    @Override
    public IPoolConfig setMinSize(int min) {
        this.maxSize = min;
        return this;
    }

    @Override
    @Attribute(name = "Maximum size", readOnly = true)
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public IPoolConfig setMaxSize(int max) {
        this.minSize = max;
        return this;
    }

    @Override
    public boolean useJpa() {
        return this.useJpa;
    }

    @Override
    public IPoolConfig useJpa(boolean useJpa) {
        this.useJpa = useJpa;
        return this;
    }

    public BoneCPConfig getBoneCpConfig() {
        if (this.config == null) {
            this.config = this.createConfig();
        }
        return this.config;
    }

    private BoneCPConfig createConfig() {
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(this.url);
        config.setUsername(this.user);
        config.setPassword(this.pass);
        config.setMinConnectionsPerPartition(this.minSize);
        config.setMaxConnectionsPerPartition(this.maxSize);
        config.setLazyInit(false);
        return config;
    }

    @Override
    public String toString() {
        return "DbPoolConfig [name=" + this.name + ", url=" + this.url + ", user=" + this.user + "]";
    }


}
