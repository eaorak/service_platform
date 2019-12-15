package com.adenon.sp.persistence.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.kernel.utils.Properties;
import com.adenon.sp.persistence.Dao;
import com.adenon.sp.persistence.IDbPool;
import com.adenon.sp.persistence.SystemPool;
import com.adenon.sp.persistence.jpa.EmHolder;
import com.adenon.sp.persistence.jpa.JpaProps;
import com.jolbox.bonecp.BoneCP;


public class DbPool implements IDbPool {

    private final BoneCP pool;
    private DbPoolConfig config;
    private EmHolder     emHolder;

    public static DbPool createSystemPool(SystemPool sysPool,
                                          Properties props,
                                          Services services,
                                          String unitName) throws Exception {
        return createPool(DbPoolConfig.createConfig(sysPool, props), unitName, services);
    }

    public static DbPool createPool(DbPoolConfig config,
                                    String unitName,
                                    Services services) throws Exception {
        IAdministrationService administration = services.getService(IAdministrationService.class);
        DbPool dbPool = new DbPool(config);
        //
        Map<String, Object> jpaProps = JpaProps.getPropsFor(config);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitName, jpaProps);
        dbPool.emHolder = new EmHolder(emf, emf.createEntityManager(jpaProps));
        //
        dbPool.config = administration.registerBean(dbPool.getConfig());
        return dbPool;
    }

    private DbPool(DbPoolConfig config) throws SQLException {
        this.config = config;
        this.pool = new BoneCP(config.getBoneCpConfig());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.pool.getConnection();
    }

    @Override
    public DbPoolConfig getConfig() {
        return this.config;
    }

    @Override
    public Dao newDao() throws SQLException {
        return Dao.newDao(this.getConnection());
    }

    @Override
    public EntityManager getEntityManager() {
        return this.emHolder.manager();
    }

    public EmHolder getEmHolder() {
        return this.emHolder;
    }


    public void close() {
        this.pool.close();
    }

}