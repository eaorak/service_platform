package com.adenon.sp.channels.config;

import java.lang.reflect.Field;

import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Dynamic;


@MBean(parent = ChannelMessageBean.class)
@Dynamic(key = "name", value = "type")
public class FieldInfoBean {

    private String name;
    private String type;

    @Deprecated
    public FieldInfoBean() {
    }

    public FieldInfoBean(Field f) {
        String cname = f.getDeclaringClass().getSimpleName();
        this.name = cname + "." + f.getName();
        this.type = f.getType().getName();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }
}
