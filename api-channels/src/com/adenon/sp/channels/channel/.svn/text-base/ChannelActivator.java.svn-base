package com.adenon.sp.channels.channel;

import java.util.List;

import org.osgi.framework.Bundle;

import com.adenon.sp.channels.IChannelRegistry;
import com.adenon.sp.channels.api.AbstractProvider;
import com.adenon.sp.channels.api.ApiMessaging;
import com.adenon.sp.channels.api.IApiMessaging;
import com.adenon.sp.channels.channel.messaging.IChannelProtocol;
import com.adenon.sp.channels.channel.messaging.IMessageRegistry;
import com.adenon.sp.channels.messaging.IChannelMessaging;
import com.adenon.sp.kernel.event.SubsystemType;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.osgi.IOsgiUtilService;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.kernel.osgi.activator.Activator;
import com.adenon.sp.kernel.utils.IBundleScanService;
import com.adenon.sp.kernel.utils.StringUtils;
import com.adenon.sp.kernel.utils.log.BundleType;
import com.adenon.sp.streams.IStreamService;


public abstract class ChannelActivator extends Activator {

    protected IChannelMessaging channelMessaging;
    protected IApiMessaging     apiMessaging;
    //
    private IChannelRegistry    registry;
    private IChannelInfo        channelInfo;

    public ChannelActivator() {
    }

    @Override
    public final BundleType getType() {
        return BundleType.CHANNEL;
    }

    @Override
    protected final void start() throws Exception {
        this.checkParameters();
        //
        this.getServices(IBundleScanService.class, IStreamService.class, IMessageRegistry.class, IOsgiUtilService.class);
        this.registry = this.getService(IChannelRegistry.class);
        //
        Bundle apiBundle = this.findApiBundle();
        ChannelParams params = this.createParams(apiBundle);
        // Register messages
        IMessageRegistry messageRegistry = this.services.getService(IMessageRegistry.class);
        messageRegistry.registerMessage(this.getShortName(), params.getMessages());
        //
        IChannelInfoInternal info = this.registry.register(this.bundleInfo, params);
        this.channelMessaging = info.getMessaging();
        this.apiMessaging = new ApiMessaging(info, this.services);
        this.channelInfo = info;
        //
        info.setProtocol(this.getProtocol());
        try {
            this.startChannel();
        } catch (Exception e) {
            this.stop();
            throw e;
        }
    }

    @Override
    protected final void stop() throws Exception {
        try {
            this.stopChannel();
        } finally {
            this.registry.unregister(this.channelInfo);
        }
    }

    @Override
    protected <T> T registerService(final Class<T> inf,
                                    final T service) {
        if (!(service instanceof AbstractProvider)) {
            throw new RuntimeException("Channels can only register provider services which extend [" + AbstractProvider.class.getName() + "] ! OK ?");
        }
        return super.registerService(inf, service);
    }

    private ChannelParams createParams(Bundle apiBundle) throws Exception {
        IBundleScanService classUtils = this.services.getService(IBundleScanService.class);
        List<Class<AbstractMessage>> messages = classUtils.getExtendedClassesOf(apiBundle, AbstractMessage.class);
        ChannelParams params = new ChannelParams();
        params.setShortName(this.getShortName());
        params.setSubsystem(this.getSubsystem());
        params.setActivator(this);
        params.setMessages(messages);
        return params;
    }

    private Bundle findApiBundle() {
        String channelName = this.bundleInfo.get(ManifestHeaders.BUNDLE_SYMBOLICNAME);
        String apiBundleName = channelName + "-api";
        Bundle apiBundle = this.services.getService(IOsgiUtilService.class).findBundle(this.context, apiBundleName);
        if (apiBundle == null) {
            throw new RuntimeException("Api bundle (" + apiBundleName + ") could not be found for channel (" + channelName + ") !");
        }
        return apiBundle;
    }

    private void checkParameters() throws Exception {
        String shortName = this.getShortName();
        if ((shortName == null) || shortName.contains(" ") || (shortName.length() > 15) || !StringUtils.allUpperCase(shortName)) {
            throw new Exception("Short name can not be NULL, can not contain spaces, should consist of uppercase and "
                                + "should be shorter than 15 characters (like SMS, MMS, USSD, SIP etc)");
        }
        SubsystemType subsystem = this.getSubsystem();
        if (subsystem == null) {
            throw new Exception("Subsystem can not be null !");
        }
    }

    // ____ Lifecycle ____

    protected abstract void startChannel() throws Exception;

    protected abstract void stopChannel() throws Exception;

    // ____ Information ____

    protected abstract IChannelProtocol getProtocol();

    protected abstract SubsystemType getSubsystem();

    protected abstract String getShortName();

}
