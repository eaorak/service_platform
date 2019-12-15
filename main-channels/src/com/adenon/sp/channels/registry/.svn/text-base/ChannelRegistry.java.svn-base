package com.adenon.sp.channels.registry;

import java.util.List;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.channels.ChannelInfo;
import com.adenon.sp.channels.IChannelRegistry;
import com.adenon.sp.channels.channel.ChannelParams;
import com.adenon.sp.channels.channel.IChannelInfo;
import com.adenon.sp.channels.channel.IChannelInfoInternal;
import com.adenon.sp.channels.config.ChannelMessageBean;
import com.adenon.sp.channels.messaging.ChannelMessaging;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.osgi.BundleInfo;
import com.adenon.sp.kernel.osgi.ManifestHeaders;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.monitoring.counters.IMonitor;
import com.adenon.sp.monitoring.counters.IMonitoringService;
import com.adenon.sp.streams.IEventProcessor;
import com.adenon.sp.streams.IStreamService;


public class ChannelRegistry implements IChannelRegistry {

    private final ChannelCache           cache = new ChannelCache();
    private final IStreamService         flowService;
    private final IEventProcessor        dispatcher;
    private final IAdministrationService configuration;
    private final IMonitoringService     monitoring;
    private final IMonitor               monitor;

    public ChannelRegistry(Services services,
                           IEventProcessor messageRouter) throws Exception {
        this.flowService = services.getService(IStreamService.class);
        this.configuration = services.getService(IAdministrationService.class);
        this.monitoring = services.getService(IMonitoringService.class);
        //
        this.monitor = this.monitoring.createMonitor("Channels");
        this.dispatcher = messageRouter;
    }

    @Override
    public IChannelInfoInternal register(BundleInfo bundle,
                                         ChannelParams params) throws Exception {
        List<Class<AbstractMessage>> messages = params.getMessages();
        BundleInfo headers = bundle;
        int bundleId = bundle.getBundleId();
        String enablerName = headers.get(ManifestHeaders.BUNDLE_SYMBOLICNAME);
        this.cache.check(enablerName, bundleId, params.getShortName());
        ChannelInfo channelInfo = this.createChannelInfo(params, bundleId, enablerName);
        //
        channelInfo = this.configuration.registerBean(channelInfo);
        for (Class<?> msg : messages) {
            ChannelMessageBean message = new ChannelMessageBean(params.getShortName(), msg);
            message.register(this.configuration);
        }
        this.cache.add(channelInfo);
        return channelInfo;
    }

    private ChannelInfo createChannelInfo(ChannelParams params,
                                          int bundleId,
                                          String enablerName) {
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setId(bundleId);
        channelInfo.setName(enablerName);
        channelInfo.setShortName(params.getShortName());
        channelInfo.setSubsystem(params.getSubsystem());
        channelInfo.setActivator(params.getActivator());
        ChannelCounters counters = this.createCounters(params.getShortName());
        channelInfo.setCounters(counters);
        ChannelMessaging messaging = new ChannelMessaging(channelInfo, this.flowService, this.dispatcher, counters);
        channelInfo.setMessaging(messaging);
        return channelInfo;
    }

    private ChannelCounters createCounters(String shortName) {
        return new ChannelCounters(this.monitor, shortName);
    }

    @Override
    public void unregister(IChannelInfo enabler) throws Exception {
        ChannelInfo removed = this.cache.remove(enabler);
        this.configuration.unregisterBean(removed);
    }

    @Override
    public ChannelInfo findById(int enablerId) {
        return this.cache.getById(enablerId);
    }

    @Override
    public IChannelInfo findByShortName(String enablerShortName) {
        return this.cache.getByShortName(enablerShortName);
    }

    public IMonitor getMonitor() {
        return this.monitor;
    }

}
