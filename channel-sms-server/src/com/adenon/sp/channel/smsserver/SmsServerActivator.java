package com.adenon.sp.channel.smsserver;

import java.util.List;

import org.apache.log4j.Level;

import com.adenon.api.smpp.sdk.LogType;
import com.adenon.library.common.sequence.ISequenceGeneratorFactory;
import com.adenon.library.common.sequence.LongSequenceGenerator;
import com.adenon.smpp.server.api.ServerApi;
import com.adenon.smpp.server.api.ServerApiEngine;
import com.adenon.smpp.server.api.ServerLoggerApi;
import com.adenon.smpp.server.core.ServerApiProperties;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.channel.smsserver.api.SmsServerConstants;
import com.adenon.sp.channel.smsserver.api.message.operation.ISmsServerSendOperations;
import com.adenon.sp.channel.smsserver.beans.LoggerBean;
import com.adenon.sp.channel.smsserver.beans.ServerConfiguration;
import com.adenon.sp.channel.smsserver.beans.UserBean;
import com.adenon.sp.channel.smsserver.beans.UsersBean;
import com.adenon.sp.channel.smsserver.configuration.mapper.LogDescriptorMapper;
import com.adenon.sp.channel.smsserver.messaging.SmsServerChannelProtocol;
import com.adenon.sp.channel.smsserver.provider.SmsServerMessageProvider;
import com.adenon.sp.channel.smsserver.proxy.SmsServerProxy;
import com.adenon.sp.channels.channel.ChannelActivator;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.event.SubsystemType;


public class SmsServerActivator extends ChannelActivator {

    public static final String       ENGINE_NAME = "adenonSmsEngine";
    private IAdministrationService   administrationService;
    private UsersBean                usersBean;
    private SmsServerProxy           proxy;
    private LongSequenceGenerator    longSequenceGenerator;
    private ServerApi                serverApi;
    private SmsServerChannelProtocol channelProtocol;

    @Override
    public SubsystemType getSubsystem() {
        return SubsystemType.NETWORK;
    }

    @Override
    public String getShortName() {
        return SmsServerConstants.SMS_SERVER_CHANNEL_NAME;
    }

    @Override
    public void startChannel() throws Exception {
        try {
            ISequenceGeneratorFactory sequenceGeneratorFactory = this.getService(ISequenceGeneratorFactory.class);
            this.setLongSequenceGenerator(sequenceGeneratorFactory.getLongSequenceGenerator("smsServerChannel"));

            this.proxy = new SmsServerProxy(this, this.channelMessaging);

            this.administrationService = this.getService(IAdministrationService.class);

            IBeanHelper<LoggerBean> loggerBeanHelper = this.administrationService.getBeans(LoggerBean.class);
            LoggerBean smsApiLogBean = null;
            smsApiLogBean = this.processApiBean(loggerBeanHelper);
            if (smsApiLogBean == null) {
                throw new NullPointerException("Logger Bean is null!!");
            }

            IBeanHelper<UsersBean> userBeanHelper = this.administrationService.getBeans(UsersBean.class);
            this.setUsersBean(new UsersBean(this));
            userBeanHelper.service().registerBean(new UsersBean(this));

            IBeanHelper<ServerConfiguration> serverConfigBeans = this.administrationService.getBeans(ServerConfiguration.class);
            List<ServerConfiguration> allBeans = serverConfigBeans.getAllBeans();
            ServerApiProperties serverApiProperties = new ServerApiProperties();

            ServerConfiguration serverConfiguration;

            if ((allBeans != null) && (allBeans.size() > 0)) {
                serverConfiguration = allBeans.get(0);
                serverConfigBeans.register();
            } else {
                serverConfiguration = new ServerConfiguration();
                serverConfigBeans.service().registerBean(serverConfiguration);
            }

            serverApiProperties.setMaxThreadCount(serverConfiguration.getMaxThreadCount());
            serverApiProperties.setPort(serverConfiguration.getPort());
            serverApiProperties.setThreadCount(serverConfiguration.getThreadCount());
            serverApiProperties.setTraceOn(serverConfiguration.getTraceOn());
            serverApiProperties.setWindowSize(serverConfiguration.getWindowSize());

            IBeanHelper<UserBean> beans = this.administrationService.getBeans(UserBean.class);
            List<UserBean> allUsers = beans.register();
            for (UserBean userDescriptionBean : allUsers) {
                this.getUsersBean().addUser(userDescriptionBean);
            }

            ServerApiEngine apiEngine = ServerApiEngine.getServerApiEngine("myServer");
            this.serverApi = apiEngine.getServerApi(this.proxy, LogDescriptorMapper.get(smsApiLogBean), serverApiProperties);
            ServerLoggerApi serverLoggerApi = this.serverApi.getServerLoggerApi();
            this.channelProtocol.setServerMessagingApi(this.serverApi.getServerMessagingApi());
            smsApiLogBean.setSmppLoggerApi(serverLoggerApi);

            this.registerService(ISmsServerSendOperations.class, new SmsServerMessageProvider(this.apiMessaging));

        } catch (Exception e) {
            throw e;
        }
    }

    private LoggerBean processApiBean(IBeanHelper<LoggerBean> loggerBeanHelper) throws Exception {
        LoggerBean smsApiLogBean;
        List<LoggerBean> allBeans = loggerBeanHelper.getAllBeans();
        if (allBeans.size() == 0) {
            smsApiLogBean = new LoggerBean();
            smsApiLogBean.setLogType(LogType.LogAllInOneFile.getValue());
            smsApiLogBean.setLogLevel(Level.INFO_INT);
            smsApiLogBean.setWriteConsole(false);
            loggerBeanHelper.service().registerBean(smsApiLogBean);
        } else {
            smsApiLogBean = allBeans.get(0);
            loggerBeanHelper.register();
        }
        return smsApiLogBean;
    }

    @Override
    public void stopChannel() throws Exception {

    }

    @Override
    protected IChannelProtocol getProtocol() {
        this.channelProtocol = new SmsServerChannelProtocol(this, this.channelMessaging);
        return this.channelProtocol;
    }

    public IAdministrationService getAdministrationService() {
        return this.administrationService;
    }

    public UsersBean getUsersBean() {
        return this.usersBean;
    }

    public void setUsersBean(UsersBean usersBean) {
        this.usersBean = usersBean;
    }

    public LongSequenceGenerator getLongSequenceGenerator() {
        return this.longSequenceGenerator;
    }

    public void setLongSequenceGenerator(LongSequenceGenerator longSequenceGenerator) {
        this.longSequenceGenerator = longSequenceGenerator;
    }

    public ServerApi getServerApi() {
        return this.serverApi;
    }

}
