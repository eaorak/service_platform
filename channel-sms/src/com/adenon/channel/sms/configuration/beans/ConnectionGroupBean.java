package com.adenon.channel.sms.configuration.beans;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.adenon.channel.sms.SmsActivator;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;


@MBean(parent = SmsBean.class, persist = true, id = "connectionGroupName")
public class ConnectionGroupBean {

    private final Map<String, ConnectionBean> connectiontable = new Hashtable<String, ConnectionBean>();
    private String                            connectionGroupName;
    private SmsActivator                      smsActivator;

    public ConnectionGroupBean() {
    }

    public ConnectionGroupBean(final SmsActivator smsActivator) {
        this.setSmsActivator(smsActivator);
    }

    @Attribute
    public String getConnectionGroupName() {
        return this.connectionGroupName;
    }

    public void setConnectionGroupName(final String connectionGroupName) {
        this.connectionGroupName = connectionGroupName;
    }

    @Operation(name = "Add Connection")
    public String addConnection(@Parameter(name = "Connection Name") final String connectionName,
                                @Parameter(name = "username") final String username,
                                @Parameter(name = "password") final String password,
                                @Parameter(name = "port") final int port,
                                @Parameter(name = "ip list", desc = "please enter ips with delimeter ,") final String ipList,
                                @Parameter(name = "window size") final int smppWindowSize,
                                @Parameter(name = "connection type") final int connectionType,
                                @Parameter(name = "trace") final boolean trace,
                                @Parameter(name = "max thread count to read") final int maxThreadCount) throws Exception {

        if (this.checkStringIsEmpty(connectionName)) {
            return "Connection name must be filled.";
        }
        if (this.connectiontable.containsKey(connectionName)) {
            return "Connection name : " + connectionName + " has already been created. Please remove first.";

        }
        final ConnectionBean connectionBean = new ConnectionBean();
        connectionBean.setConnectionGroupName(this.connectionGroupName);
        connectionBean.setConnectionName(connectionName);
        connectionBean.setUsername(username);
        connectionBean.setPassword(password);
        connectionBean.setPort(port);
        connectionBean.setConnectionIpList(ipList);
        connectionBean.setSmppWindowSize(smppWindowSize);
        connectionBean.setConnectionType(connectionType);
        connectionBean.setTrace(trace);
        connectionBean.setMaxThreadCount(maxThreadCount);

        IBeanHelper<ConnectionBean> beans = this.getSmsActivator().getConfigurationService().getBeans(ConnectionBean.class);
        beans.service().registerBean(connectionBean);

        this.addConnection(connectionBean, true);

        return "Connection name : " + this.connectionGroupName + " has been sucessfully created. ";
    }

    @Operation(name = "Add Default Connection")
    public String addConnection(@Parameter(name = "Connection Name") final String connectionName) throws Exception {

        if (this.checkStringIsEmpty(connectionName)) {
            return "Connection name must be filled.";
        }
        if (this.connectiontable.containsKey(connectionName)) {
            return "Connection name : " + connectionName + " has already been created. Please remove first.";

        }
        final ConnectionBean connectionBean = new ConnectionBean();
        connectionBean.setConnectionGroupName(this.connectionGroupName);
        connectionBean.setConnectionName(connectionName);
        connectionBean.setUsername("username");
        connectionBean.setPassword("password");
        connectionBean.setPort(5101);
        connectionBean.setConnectionIpList("127.0.0.1");
        connectionBean.setSmppWindowSize(50);
        connectionBean.setConnectionType(1);
        connectionBean.setTrace(false);
        connectionBean.setMaxThreadCount(5);
        connectionBean.setSmsActivator(this.smsActivator);

        this.addConnection(connectionBean, false);

        IBeanHelper<ConnectionBean> beans = this.getSmsActivator().getConfigurationService().getBeans(ConnectionBean.class);
        beans.service().registerBean(connectionBean);
        return "Connection name : " + this.connectionGroupName + " has been sucessfully created. ";
    }

    public void addConnection(ConnectionBean connectionBean, boolean initiateConnection) throws Exception {

        this.connectiontable.put(connectionBean.getConnectionName(), connectionBean);

        connectionBean.setConnectionApi(this.getSmsActivator().getSmppApi().getSmppConnectionApi());

        if (initiateConnection) {
            try {
                connectionBean.setConnectionActive(true);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }

    }

    @Operation
    public boolean removeConnection(final String connectionName) {
        try {
            if ((connectionName == null) || "".equals(connectionName)) {
                return false;
            }
            IBeanHelper<ConnectionBean> beans = this.getSmsActivator().getConfigurationService().getBeans(ConnectionBean.class);
            List<ConnectionBean> allBeans = beans.getAllBeans();
            for (ConnectionBean connectionBean : allBeans) {
                if (connectionName.equals(connectionBean.getConnectionName())) {

                }
            }
            this.connectiontable.remove(connectionName);
            return true;
        } catch (final Exception e) {
        }
        return false;
    }

    public boolean checkStringIsEmpty(final String str) {
        return (str == null) || str.equals("") ? true : false;
    }

    public ConnectionBean[] getConnectionList() {
        return this.connectiontable.values().toArray(new ConnectionBean[0]);
    }

    public SmsActivator getSmsActivator() {
        return this.smsActivator;
    }

    public void setSmsActivator(SmsActivator smsActivator) {
        this.smsActivator = smsActivator;
    }

}
