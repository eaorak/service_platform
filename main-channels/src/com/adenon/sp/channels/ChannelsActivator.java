package com.adenon.sp.channels;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.channels.IChannelRegistry;
import com.adenon.sp.channels.channel.messaging.IMessageRegistry;
import com.adenon.sp.channels.dispatch.MessageRouter;
import com.adenon.sp.channels.messaging.ChannelsEventProcessor;
import com.adenon.sp.channels.registry.ChannelRegistry;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.osgi.activator.BundleActivator;
import com.adenon.sp.monitoring.counters.IMonitoringService;
import com.adenon.sp.streams.IStreamService;
import com.adenon.sp.streams.Priority;


public class ChannelsActivator extends BundleActivator {

    private IAdministrationService configuration;

    @Override
    protected void start() throws Exception {
        this.getServices(IMonitoringService.class, IAdministrationService.class);
        IStreamService flowService = this.getService(IStreamService.class);

        this.configuration = this.services.getService(IAdministrationService.class);

        // Channel registry
        MessageRouter messageRouter = new MessageRouter(this.configuration);
        this.registerService(IMessageRegistry.class, messageRouter);

        // Message registry
        ChannelRegistry registry = new ChannelRegistry(this.services, messageRouter);
        this.registerService(IChannelRegistry.class, registry);

        //
        ChannelsEventProcessor processor = new ChannelsEventProcessor(registry);
        flowService.register(processor, Direction.TOWARDS_OUT, Priority.LAST);
    }

    @Override
    protected void stop() throws Exception {
    }

}
