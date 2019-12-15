package com.adenon.sp.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.kernel.properties.SysProps;
import com.adenon.sp.kernel.utils.Properties;
import com.adenon.sp.persistence.pool.DbPool;
import com.adenon.sp.persistence.pool.DbPoolConfig;


public class PersistenceService implements IPersistenceService {

    private final Map<String, DbPool> pools    = new ConcurrentHashMap<String, DbPool>();
    private final Services            services;
    private final Properties          dbProps;
    private final List<String>        jpaUnits = Collections.synchronizedList(new ArrayList<String>());

    {
        for (int i = 1; i <= PersistenceConstants.JPADB_COUNT; i++) {
            this.jpaUnits.add(PersistenceConstants.JPADB_PREFIX + i);
        }
    }

    public PersistenceService(Services services,
                              String confPath) throws Exception {
        this.services = services;
        IAdministrationService service = services.getService(IAdministrationService.class);
        this.dbProps = this.loadConfiguration(confPath);
        service.registerBean(new PersistenceBean(this));
        this.initSystemPools();
        // this.test();
    }

    private Properties loadConfiguration(String confPath) throws IOException {
        String filePath = confPath + SysProps.PATH_SEP.value() + PersistenceConstants.CONFIG_FILE_NAME;
        return Properties.load(filePath, SystemPool.values(), DbConfig.values());
    }

    private void initSystemPools() throws Exception {
        for (SystemPool pool : SystemPool.values()) {
            DbPool sysPool = DbPool.createSystemPool(pool, this.dbProps, this.services, this.getNewId());
            this.registerPool(sysPool);
        }
    }

    @Override
    public DbPool getSystemPool(SystemPool pool) {
        return this.pools.get(pool.key());
    }

    @Override
    public IDbPool createPool(IPoolConfig config) throws Exception {
        if (this.pools.get(config.getName()) != null) {
            throw new Exception("Pool with name [" + config.getName() + "] already exist !");
        }
        String newId = this.getNewId();
        DbPool pool = DbPool.createPool((DbPoolConfig) config, newId, this.services);
        this.registerPool(pool);
        return pool;
    }


    @Override
    public IPoolConfig newConfig(String name,
                                 String url) {
        return new DbPoolConfig(name, url);
    }

    @Override
    public IDbPool getPool(String name) {
        return this.pools.get(name);
    }


    @Override
    public boolean deletePool(String poolName) {
        if (SystemPool.isSystemPool(poolName)) {
            throw new RuntimeException("System pools can not be deleted !");
        }
        DbPool dbPool = this.pools.get(poolName);
        if (dbPool != null) {
            dbPool.close();
            return true;
        }
        return false;
    }

    private void test() throws Exception {
        IDbPool pool = this.createPool(this.newConfig("test", "jdbc:mysql://localhost:3306/spdb")
                                           .setCredentials("root", "root")
                                           .setDriver("com.mysql.jdbc.Driver"));
        Connection connection = pool.getConnection();
        System.out.println(connection);
    }

    private void registerPool(DbPool pool) {
        this.pools.put(pool.getConfig().getName(), pool);
    }


    private String getNewId() {
        String unit = this.jpaUnits.remove(0);
        if (unit == null) {
            throw new RuntimeException("Maximum allowed pool count [" + PersistenceConstants.JPADB_COUNT + "] exceeded !");
        }
        return unit;
    }

}
