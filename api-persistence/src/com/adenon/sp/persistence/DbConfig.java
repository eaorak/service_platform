package com.adenon.sp.persistence;

import com.adenon.sp.kernel.utils.IKeyEnum;


public enum DbConfig implements IKeyEnum {

    URL("url"), //
    USER("user"), //
    PASS("pass"), //
    DRIVER("driver"), //
    INIT_CONNECTION("initConn"), //
    MAX_CONNECTION("maxiConn");

    private String name;

    private DbConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public DbConfig[] allValues() {
        return values();
    }

    @Override
    public String key() {
        return this.name;
    }

}
