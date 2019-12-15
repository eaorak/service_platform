package com.adenon.sp.channel.smsserver.beans;

import java.util.HashMap;
import java.util.Map;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.channel.smsserver.SmsServerActivator;


@MBean(location = ConfigLocation.CHANNELS, subLocation = "SmsAdapter=SMSSERVER", persist = false)
public class UsersBean {

    private final Map<String, UserBean> userTable = new HashMap<String, UserBean>();
    private SmsServerActivator          smsServerActivator;

    public UsersBean() {
    }

    public UsersBean(final SmsServerActivator smsActivator) {
        this.smsServerActivator = smsActivator;
    }


    @Operation
    public String addUser(@Parameter(name = "Username") final String username,
                          @Parameter(name = "password") String password,
                          @Parameter(name = "maxConnection") int maxConnection) throws Exception {
        if (this.userTable.containsKey(username)) {
            return "User name : " + username + " has already been created. Please remove first.";
        }

        UserBean userDescriptionBean = new UserBean();
        userDescriptionBean.setMaxConnection(maxConnection);
        userDescriptionBean.setPassword(password);
        userDescriptionBean.setUsername(username);

        this.userTable.put(username, userDescriptionBean);

        this.smsServerActivator.getAdministrationService().registerBean(userDescriptionBean);

        return "User name : " + username + " has been sucessfully created. ";
    }


    @Operation
    public String removeUser(@Parameter(name = "Username") final String username) throws Exception {
        UserBean removedUser = this.userTable.remove(username);
        if (removedUser != null) {
            this.smsServerActivator.getAdministrationService().unregisterBean(removedUser);
            return removedUser + " removed successfully";
        }
        return "Removed failed. No such user : " + removedUser;
    }

    public void addUser(UserBean userDescriptionBean) {
        this.userTable.put(userDescriptionBean.getUsername(), userDescriptionBean);

    }

    public SmsServerActivator getSmsServerActivator() {
        return this.smsServerActivator;
    }

    public void setSmsServerActivator(SmsServerActivator smsServerActivator) {
        this.smsServerActivator = smsServerActivator;
    }

    public UserBean getUser(String username) {
        return this.userTable.get(username);
    }
}
