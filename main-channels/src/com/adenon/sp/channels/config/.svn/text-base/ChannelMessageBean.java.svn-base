package com.adenon.sp.channels.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.adenon.sp.administration.AdministrationException;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Join;
import com.adenon.sp.channels.ChannelInfo;


@MBean(parent = ChannelInfo.class, subLocation = "messages=Messages", id = "simpleName")
public class ChannelMessageBean {

    private Class<?>                  message;
    private String                    channelShortName;
    private final List<FieldInfoBean> fieldList = new ArrayList<FieldInfoBean>();

    @Deprecated
    public ChannelMessageBean() {
    }

    public ChannelMessageBean(String enablerShortName,
                              Class<?> message) {
        this.channelShortName = enablerShortName;
        this.message = message;
        Field[] fields = message.getDeclaredFields();
        for (Field f : fields) {
            this.fieldList.add(new FieldInfoBean(f));
        }
    }

    @Attribute(readOnly = true)
    public String getName() {
        return this.message.getName();
    }

    @Join
    public String getChannelShortName() {
        return this.channelShortName;
    }

    public String getSimpleName() {
        return this.message.getSimpleName();
    }

    public void register(IAdministrationService administration) throws AdministrationException {
        administration.registerBean(this);
        for (FieldInfoBean info : this.fieldList) {
            // administration.registerBean(info);
        }
    }

}
