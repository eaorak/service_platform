package com.adenon.channel.sms;

import java.util.List;

import org.apache.log4j.Level;

import com.adenon.api.smpp.SmppApi;
import com.adenon.api.smpp.SmppApiEngine;
import com.adenon.api.smpp.sdk.LogType;
import com.adenon.channel.sms.api.constant.SmsChannelConstants;
import com.adenon.channel.sms.api.operation.ISmsSender;
import com.adenon.channel.sms.configuration.beans.ConnectionBean;
import com.adenon.channel.sms.configuration.beans.ConnectionGroupBean;
import com.adenon.channel.sms.configuration.beans.LoggerBean;
import com.adenon.channel.sms.configuration.beans.SmsBean;
import com.adenon.channel.sms.configuration.mapper.LogDescriptorMapper;
import com.adenon.channel.sms.messaging.SmsChannelProtocol;
import com.adenon.channel.sms.provider.SmsMessageProvider;
import com.adenon.channel.sms.proxy.SmsApiProxy;
import com.adenon.library.common.sequence.ISequenceGeneratorFactory;
import com.adenon.library.common.sequence.LongSequenceGenerator;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.channels.channel.ChannelActivator;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.kernel.event.SubsystemType;


public class SmsActivator extends ChannelActivator {

    public static final String     ENGINE_NAME = "adenonSmsEngine";
    private SmppApiEngine          smppApiEngine;
    private SmppApi                smppApi;
    private SmsApiProxy            smsApiProxy;
    private IAdministrationService administrationService;
    private LongSequenceGenerator  longSequenceGenerator;

    @Override
    public SubsystemType getSubsystem() {
        return SubsystemType.NETWORK;
    }

    @Override
    public String getShortName() {
        return SmsChannelConstants.SMS_CHANNEL_NAME;
    }

    @Override
    public void startChannel() throws Exception {
        try {
            this.smsApiProxy = new SmsApiProxy(this.channelMessaging);

            ISequenceGeneratorFactory sequenceGeneratorFactory = this.getService(ISequenceGeneratorFactory.class);
            this.longSequenceGenerator = sequenceGeneratorFactory.getLongSequenceGenerator("smsChannel");

            this.administrationService = this.getService(IAdministrationService.class);

            IBeanHelper<LoggerBean> loggerBeanHelper = this.administrationService.getBeans(LoggerBean.class);

            LoggerBean smsApiLogBean = null;
            smsApiLogBean = this.processApiBean(loggerBeanHelper);
            if (smsApiLogBean == null) {
                throw new NullPointerException("Logger Bean is null!!");
            }

            // TODO : implement proper tps counter
            this.smppApiEngine = SmppApiEngine.getSmppApiEngine(ENGINE_NAME, 100);

            this.setSmppApi(this.smppApiEngine.getSmppApi(LogDescriptorMapper.get(smsApiLogBean)));

            smsApiLogBean.setSmppLoggerApi(this.getSmppApi().getSmppLoggerApi());

            IBeanHelper<SmsBean> tZeroBeanHelper = this.administrationService.getBeans(SmsBean.class);
            SmsBean processTZeroBean = this.processTZeroBean(tZeroBeanHelper);

            IBeanHelper<ConnectionGroupBean> conGrpHelper = this.administrationService.getBeans(ConnectionGroupBean.class);
            IBeanHelper<ConnectionBean> conBeans = this.administrationService.getBeans(ConnectionBean.class);

            this.processConnectionGroupBeans(conGrpHelper, conBeans, processTZeroBean);

            this.registerService(ISmsSender.class, new SmsMessageProvider(this.apiMessaging));

        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void stopChannel() throws Exception {

    }

    @Override
    protected IChannelProtocol getProtocol() {
        return new SmsChannelProtocol(this, this.channelMessaging);
    }

    private void processConnectionGroupBeans(IBeanHelper<ConnectionGroupBean> conGrpHelper,
                                             IBeanHelper<ConnectionBean> conBeans,
                                             SmsBean processTZeroBean) throws Exception {

        List<ConnectionGroupBean> conGrpList = conGrpHelper.getAllBeans();
        for (ConnectionGroupBean groupBean : conGrpList) {
            groupBean.setSmsActivator(this);
            processTZeroBean.addConnectionGroup(groupBean.getConnectionGroupName());
            IBeanHelper<ConnectionBean> groupConnections = conBeans.getChildsOf(groupBean);
            List<ConnectionBean> connectionBeans = groupConnections.register();
            for (ConnectionBean connectionBean : connectionBeans) {
                connectionBean.setSmsActivator(this);
                groupBean.addConnection(connectionBean, connectionBean.getConnectionActive());
            }
        }
    }

    private LoggerBean processApiBean(IBeanHelper<LoggerBean> helper) throws Exception {
        LoggerBean smsApiLogBean;
        List<LoggerBean> allBeans = helper.getAllBeans();
        if (allBeans.size() == 0) {
            if (this.smppApi != null) {
                smsApiLogBean = new LoggerBean(this.smppApi.getSmppLoggerApi());
            } else {
                smsApiLogBean = new LoggerBean();
            }
            smsApiLogBean.setLogType(LogType.LogAllInOneFile.getValue());
            smsApiLogBean.setLogLevel(Level.INFO_INT);
            smsApiLogBean.setWriteConsole(false);
            helper.service().registerBean(smsApiLogBean);
        } else {
            smsApiLogBean = allBeans.get(0);
            helper.register();
        }
        return smsApiLogBean;
    }

    private SmsBean processTZeroBean(IBeanHelper<SmsBean> tZeroBeanHelper) throws Exception {
        List<SmsBean> tZeroBeans = tZeroBeanHelper.getAllBeans();
        SmsBean tZeroBean = null;
        if (tZeroBeans.size() == 0) {
            tZeroBean = new SmsBean(this);
            tZeroBeanHelper.service().registerBean(tZeroBean);
        } else {
            tZeroBean = tZeroBeans.get(0);
            tZeroBean.setSmsActivator(this);
            tZeroBeanHelper.register();
        }
        return tZeroBean;
    }

    public SmppApi getSmppApi() {
        return this.smppApi;
    }

    public void setSmppApi(SmppApi smppApi) {
        this.smppApi = smppApi;
    }

    public SmsApiProxy getSmsApiProxy() {
        return this.smsApiProxy;
    }

    public void setSmsApiProxy(SmsApiProxy smsApiProxy) {
        this.smsApiProxy = smsApiProxy;
    }

    public IAdministrationService getConfigurationService() {
        return this.administrationService;
    }

    public void setConfigurationService(IAdministrationService configurationService) {
        this.administrationService = configurationService;
    }

    public LongSequenceGenerator getLongSequenceGenerator() {
        return this.longSequenceGenerator;
    }
}
