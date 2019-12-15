package com.adenon.channel.sss.configuration;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.annotate.Attribute;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.kernel.osgi.Services;


@MBean(parent = SmsBean.class, persist = true, id = "name")
public class SmsConnectionGroup {

    private final Map<String, SmsConnection> connections = new Hashtable<String, SmsConnection>();
    private String                           name;
    private IAdministrationService           administration;

    @Deprecated
    public SmsConnectionGroup() {
    }

    public SmsConnectionGroup(Services services) {
        this.administration = services.getService(IAdministrationService.class);
    }

    public void initialize(Services services, List<SmsConnection> connectionList) {
        this.administration = services.getService(IAdministrationService.class);
        for (SmsConnection connection : connectionList) {
            this.connections.put(connection.getConnectionName(), connection);
        }
    }

    @Attribute
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Operation(name = "Add Connection")
    public String addConnection(@Parameter(name = "Connection Name") final String connectionName,
                                @Parameter(name = "User Name") final String username,
                                @Parameter(name = "Password") final String password,
                                @Parameter(name = "Port") final int port) throws Exception {

        if (this.checkStringIsEmpty(connectionName)) {
            return "Connection name must be filled.";
        }
        if (this.connections.containsKey(connectionName)) {
            return "Connection name : " + connectionName + " has already been created. Please remove first.";

        }
        final SmsConnection connection = new SmsConnection();
        connection.setGroupName(this.name);
        connection.setConnectionName(connectionName);
        connection.setUsername(username);
        connection.setPassword(password);
        connection.setPort(port);

        this.administration.registerBean(connection);
        this.connections.put(connectionName, connection);

        return "Connection name : " + this.name + " has been sucessfully created. ";
    }

    @Operation(name = "Remove Connection")
    public boolean removeConnection(@Parameter(name = "Connection Name") final String connectionName) {
        try {
            SmsConnection connection = this.connections.remove(connectionName);
            this.administration.unregisterBean(connection);
            return true;
        } catch (final Exception e) {
        }
        return false;
    }

    public boolean checkStringIsEmpty(final String str) {
        if (str == null) {
            return true;
        }
        if ("".equals(str)) {
            return true;
        }
        return false;
    }

}
