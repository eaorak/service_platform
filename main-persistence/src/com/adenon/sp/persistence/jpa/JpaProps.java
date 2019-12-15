package com.adenon.sp.persistence.jpa;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.adenon.sp.persistence.IPersistenceService;
import com.adenon.sp.persistence.pool.DbPoolConfig;

public enum JpaProps {

    JPA_PRS_PROVIDER("javax.persistence.provider"), //
    JDBC_URL("javax.persistence.jdbc.url"), //
    JDBC_USER("javax.persistence.jdbc.user"), //
    JDBC_PASSWORD("javax.persistence.jdbc.password"), //
    JDBC_DRIVER("javax.persistence.jdbc.driver"), //
    //
    HBN_DDL_AUTO("hibernate.hbm2ddl.auto"), //
    HBN_RSC_SCANNER("hibernate.ejb.resource_scanner"); //

    private final String key;

    private JpaProps(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }

    public static Map<String, Object> getPropsFor(DbPoolConfig config) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(JDBC_URL.key(), config.getUrl());
        props.put(JDBC_DRIVER.key(), config.getDriver());
        props.put(JDBC_USER.key(), config.getUser());
        props.put(JDBC_PASSWORD.key(), config.getRealPass());
        props.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
        props.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
        props.put(PersistenceUnitProperties.CLASSLOADER, IPersistenceService.class.getClassLoader());
        props.put("eclipselink.weaving", "true");
        return props;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
