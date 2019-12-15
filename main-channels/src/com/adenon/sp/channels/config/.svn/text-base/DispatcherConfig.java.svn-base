package com.adenon.sp.channels.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;


@MBean(parent = ChannelsConfiguration.class, subLocation = "dispatch=Dispatching Rules", id = "shortName", persist = true)
public class DispatcherConfig {

    private String       rule;
    private String       className;
    private String       shortName;
    private Serializable expression;

    public DispatcherConfig() {
    }

    @Attribute
    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Attribute
    public String getRule() {
        return this.rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Serializable getExpression() {
        return this.expression;
    }

    public void setExpression(Serializable expression) {
        this.expression = expression;
    }

    @Operation(name = "Test expression", desc = "Refer message as 'msg' in expression.")
    public String testExpression(@Parameter(name = "Expression", desc = "Semicolon seperated MVEL expressions.") String expressions) {
        Object message = null;
        try {
            Class<?> msgClass = Class.forName(this.className);
            message = msgClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Message class could not be found !");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", message);
        MVEL.eval(expressions, map);
        Object result = MVEL.executeExpression(this.expression, map);
        return "Result is : " + result;
    }

    public String getShortName() {
        if (this.shortName == null) {
            this.shortName = this.className.substring(this.className.lastIndexOf(".") + 1);
        }
        return this.shortName;
    }

}
