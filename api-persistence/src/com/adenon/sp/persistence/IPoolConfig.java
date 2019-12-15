package com.adenon.sp.persistence;

public interface IPoolConfig {

    String getName();

    String getUrl();

    String getDriver();

    String getUser();

    int getMinSize();

    int getMaxSize();

    boolean useJpa();

    IPoolConfig setCredentials(String user,
                               String pass);

    IPoolConfig setMinSize(int min);

    IPoolConfig setMaxSize(int max);

    IPoolConfig setDriver(String driver);

    IPoolConfig useJpa(boolean useJpa);

}
