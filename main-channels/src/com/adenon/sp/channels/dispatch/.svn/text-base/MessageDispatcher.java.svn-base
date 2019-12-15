package com.adenon.sp.channels.dispatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.mvel2.MVEL;

import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.channels.config.ChannelsConfiguration;
import com.adenon.sp.channels.config.DispatcherConfig;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.rpc.RPCMessage;
import com.adenon.sp.streams.IEventProcessor;


public class MessageDispatcher implements IEventProcessor {

    private static Logger                         logger      = Logger.getLogger(MessageDispatcher.class);
    private final Map<Class<?>, DispatcherConfig> expressions = new ConcurrentHashMap<Class<?>, DispatcherConfig>();
    private final String                          template;
    private final IAdministrationService          configuration;

    public MessageDispatcher(final IAdministrationService configuration) throws Exception {
        this.configuration = configuration;
        this.template = this.createTemplate();
        ChannelsConfiguration channelConfig = new ChannelsConfiguration();
        this.configuration.registerBean(channelConfig);

        final IBeanHelper<DispatcherConfig> dispatchers = this.configuration.getBeans(DispatcherConfig.class);
        for (final DispatcherConfig dsp : dispatchers.getAllBeans()) {
            this.registerDispatcher(dsp);
        }

    }

    private String createTemplate() {
        final StringBuffer temp = new StringBuffer();
        temp.append("import #msg#;");
        temp.append("def dispatch(p_msg) {");
        temp.append("   service = null;");
        temp.append("   message = (#msg#) p_msg;");
        temp.append("   #dispatcher#");
        temp.append("   return service;");
        temp.append("}");
        temp.append("serviceId = dispatch(msg);");
        temp.append("if (serviceId == null) {");
        temp.append("   throw new RuntimeException('No service could be found for message ['+#msg#.name+']');");
        temp.append("}");
        temp.append("event.dialog.endpoint = serviceId;");
        return temp.toString();
    }

    public void registerDispatcher(final Class<? extends AbstractMessage> message,
                                   final String expression) throws Exception {
        DispatcherConfig dispatcher = this.expressions.get(message);
        if (dispatcher == null) {
            dispatcher = new DispatcherConfig();
            dispatcher.setClassName(message.getName());
        }
        dispatcher.setRule(expression);
        this.registerDispatcher(dispatcher);
    }

    private void registerDispatcher(final DispatcherConfig dispatcher) throws Exception {
        final Class<?> msgClass = Class.forName(dispatcher.getClassName());
        if (logger.isInfoEnabled()) {
            logger.info("[MessageDispatcher][removeDispatcher] : Removing rule for [" + msgClass.getName() + "].");
        }
        final String expression = this.template.replace("#msg#", dispatcher.getClassName()).replace("#dispatcher#", dispatcher.getRule());
        dispatcher.setExpression(MVEL.compileExpression(expression));
        if (this.expressions.get(msgClass) == null) {
            this.configuration.registerBean(dispatcher);
            this.expressions.put(msgClass, dispatcher);
        } else {
            this.configuration.unregisterBean(dispatcher);
            this.configuration.registerBean(dispatcher);
        }
    }

    public void removeDispatcher(final Class<? extends AbstractMessage> msgClass) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("[MessageDispatcher][removeDispatcher] : Removing rule for [" + msgClass.getName() + "].");
        }
        final DispatcherConfig removed = this.expressions.remove(msgClass);
        this.configuration.unregisterBean(removed);
    }

    @Override
    public IError process(final Event e) throws Exception {
        if (e.getDialog().getEndpoint() != null) {
            return null;
        }
        final Class<? extends IMessage> msgClass = e.getMessage().getClass();
        final DispatcherConfig config = this.expressions.get(msgClass);
        if (config == null) {
            // TODO : Default destination ?
            throw new Exception("No matching rule for message class : " + msgClass.getName());
        }
        // Prepare variable map
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("event", e);
        map.put("msg", e.getMessage());
        // Execute dispatch expression
        MVEL.executeExpression(config.getExpression(), map);
        return null;
    }

    @Override
    public boolean failOnError() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public void registerForTesting() throws Exception {
        StringBuffer msg = new StringBuffer();
        msg.append("   return 'com.adenon.sp.service.smsserver.service.SmsServerServiceRuntime';");
        final Class<? extends AbstractMessage> testClass = (Class<? extends AbstractMessage>) Class.forName("com.adenon.sp.channel.smsserver.api.message.SmsServerBindInfoMessage");
        this.registerDispatcher(testClass, msg.toString());
        // Rpc
        msg = new StringBuffer();
        msg.append("service='com.adenon.sp.service.test.TestService';");
        this.registerDispatcher(RPCMessage.class, msg.toString());
    }

}
