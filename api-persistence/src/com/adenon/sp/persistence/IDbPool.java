package com.adenon.sp.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;

public interface IDbPool {

    /**
     * Get a new Dao object with connection.
     */
    Dao newDao() throws SQLException;

    /**
     * Get a new connection.
     */
    Connection getConnection() throws SQLException;

    /**
     * Get pool configuration.
     */
    IPoolConfig getConfig();

    /**
     * Get JPA Entity Manager
     */
    EntityManager getEntityManager();

}
