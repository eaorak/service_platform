package com.adenon.sp.channels.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adenon.library.common.utils.StringUtils;
import com.adenon.sp.administration.IAdministrationService;
import com.adenon.sp.administration.IBeanHelper;
import com.adenon.sp.channels.channel.messaging.IMessageRegistry;
import com.adenon.sp.channels.config.ChannelsConfiguration;
import com.adenon.sp.channels.configuration.ChannelBean;
import com.adenon.sp.channels.configuration.MessageBean;
import com.adenon.sp.channels.dispatch.rules.DirectRouter;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.AbstractMessage;
import com.adenon.sp.kernel.event.message.CoreMessages;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.IMessageTypes;
import com.adenon.sp.kernel.event.message.Message;
import com.adenon.sp.kernel.event.message.MessageType;
import com.adenon.sp.kernel.event.message.rpc.RPCMessage;
import com.adenon.sp.kernel.event.message.rule.EDispatchRuleResult;
import com.adenon.sp.kernel.event.message.rule.IDispatchRule;
import com.adenon.sp.streams.IEventProcessor;



public class MessageRouter implements IEventProcessor, IMessageRegistry {

    private final Map<String, List<IDispatchRule>> messageRoutersMap = new HashMap<String, List<IDispatchRule>>();
    private final IAdministrationService           configuration;


    public MessageRouter(IAdministrationService configuration) throws Exception {
        this.configuration = configuration;

        ChannelsConfiguration channelConfig = new ChannelsConfiguration();
        this.configuration.registerBean(channelConfig);

    }

    @Override
    public IError process(Event event) throws Exception {
        IMessage message = event.getMessage();
        if (message == null) {
            throw new RuntimeException("Message Object is NULL!!!");
        }
        Enum<? extends IMessageTypes> id = message.getId();
        if (id instanceof CoreMessages) {
            CoreMessages coreMessages = (CoreMessages) id;
            if (coreMessages == CoreMessages.RPC_MESSAGE) {
                RPCMessage rpcMessage = (RPCMessage) message;
                event.getDialog().setEndpoint(rpcMessage.getServiceName());
                return null;
            }
        }
        Class<? extends IMessage> messageClass = message.getClass();

        List<IDispatchRule> list = this.messageRoutersMap.get(messageClass.getName());
        if (list == null) {
            throw new RuntimeException("No dispatch rule for " + messageClass.getName());
        }
        boolean success = false;
        for (IDispatchRule iDipatchRule : list) {
            if (iDipatchRule != null) {
                if (iDipatchRule.getActive()) {
                    if (iDipatchRule.executeRule(event) == EDispatchRuleResult.END) {
                        success = true;
                        break;
                    }
                }
            }
        }
        if (!success) {
            throw new RuntimeException("No dispatching rule(s) dispatch the messgase . Service dispatching failed !! ");
        }
        return null;
    }

    @Override
    public boolean failOnError() {
        return false;
    }


    @Override
    public void registerMessage(String channel,
                                List<Class<AbstractMessage>> messages) throws Exception {
        if (messages == null) {
            return;
        }
        for (Class<?> message : messages) {
            Message msg = message.getAnnotation(Message.class);
            if ((msg == null) || (msg.messageType() != MessageType.BEGIN)) {
                continue;
            }
            String messageName = msg.messageName();
            String[] dispatchClassNames = msg.dispatchClassNames();
            this.registerMessage(channel, messageName, message.getName(), dispatchClassNames);
        }
    }

    private void registerMessage(String channelName,
                                 String messageName,
                                 String messageClassName,
                                 String[] dispatchClassNames) throws Exception {

        if ((dispatchClassNames == null) || (dispatchClassNames.length == 0)) {
            throw new RuntimeException("Channel : " + channelName + " Message : " + messageName + " has no valid dispatcher!");
        }
        List<Class<?>> dispatchClasses = new ArrayList<Class<?>>();
        for (int i = 0; i < dispatchClassNames.length; i++) {
            Class<?> forName = Class.forName(dispatchClassNames[i]);
            Class<?> directRouter = DirectRouter.class;
            System.out.println("class : " + directRouter.getCanonicalName());
            if (!IDispatchRule.class.isAssignableFrom(forName)) {
                throw new RuntimeException("Channel : "
                                           + channelName
                                           + " Message : "
                                           + messageName
                                           + " has unvalid dispather. Class : "
                                           + dispatchClassNames[i]);
            }
            dispatchClasses.add(forName);
        }

        IBeanHelper<ChannelBean> channelBeanHelper = this.configuration.getBeans(ChannelBean.class);
        List<ChannelBean> searchedChannels = channelBeanHelper.search(channelName);

        ChannelBean channelBean;
        if (searchedChannels.size() > 0) {
            channelBean = searchedChannels.get(0);
            channelBeanHelper.register();
        } else {
            channelBean = new ChannelBean();
            channelBean.setShortName(channelName);
            this.configuration.registerBean(channelBean);
        }

        IBeanHelper<MessageBean> messageBeanHelper = this.configuration.getBeans(MessageBean.class);
        List<MessageBean> searchedMessages = messageBeanHelper.search(messageName);
        MessageBean messageBean;
        if (searchedMessages.size() > 0) {
            messageBean = searchedMessages.get(0);
            messageBeanHelper.register();
        } else {
            messageBean = new MessageBean();
            messageBean.setChannelName(channelName);
            messageBean.setClassName(messageClassName);
            messageBean.setMessageName(messageName);
            this.configuration.registerBean(messageBean);
        }
        List<IDispatchRule> dispatchRules = new ArrayList<IDispatchRule>();
        for (Class<?> clazz : dispatchClasses) {
            IBeanHelper<?> allBeansInfo = this.configuration.getBeans(clazz);
            IBeanHelper<?> childsOf = allBeansInfo.getChildsOf(messageBean);
            List<?> allBeans = childsOf.getAllBeans();
            IDispatchRule dispatchRule;
            if (allBeans.size() > 0) {
                Object object = allBeans.get(0);
                dispatchRule = (IDispatchRule) object;
                String uniqueId = dispatchRule.getUniqueId();
                if (StringUtils.checkStringIsEmpty(uniqueId)) {
                    dispatchRule.setUniqueId(StringUtils.generateUUID());
                }
                dispatchRules.add(dispatchRule);
                messageBean.registerToMessage(dispatchRule);
                childsOf.register();
            } else {
                dispatchRule = (IDispatchRule) clazz.newInstance();
                dispatchRules.add(dispatchRule);
                dispatchRule.setUniqueId(StringUtils.generateUUID());
                messageBean.registerToMessage(dispatchRule);
                this.configuration.registerBean(dispatchRule);
            }
            dispatchRule.registerBeans(this.configuration);
        }
        this.messageRoutersMap.put(messageClassName, dispatchRules);
    }
}
