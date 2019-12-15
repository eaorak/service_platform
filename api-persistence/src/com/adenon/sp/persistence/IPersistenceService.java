package com.adenon.sp.persistence;


public interface IPersistenceService {

    IDbPool getSystemPool(SystemPool pool);

    IDbPool getPool(String name);

    IDbPool createPool(IPoolConfig config) throws Exception;

    IPoolConfig newConfig(String poolName,
                          String url);

    boolean deletePool(String poolName);

}
