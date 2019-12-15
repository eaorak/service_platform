package com.adenon.sp.services.services;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.event.message.IMessage;
import com.adenon.sp.kernel.event.message.MessageKind;
import com.adenon.sp.kernel.event.message.rpc.RPCMessage;
import com.adenon.sp.kernel.execution.IRequest;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.kernel.utils.Primitives;
import com.adenon.sp.services.handler.RpcDialogHandler;
import com.adenon.sp.services.service.handler.Handler;
import com.adenon.sp.services.service.handler.IHandlerProvider;
import com.adenon.sp.services.service.info.IServiceModel;
import com.adenon.sp.services.service.info.IServiceProvider;
import com.adenon.sp.services.service.info.Service;
import com.adenon.sp.streams.IStreamService;


public class ServiceModel implements IServiceModel {

    private static final long      serialVersionUID = 1L;
    private final Class<?>         serviceClass;
    private final String           serviceId;
    private final IServiceProvider serviceBundle;
    private final IStreamService   streamService;
    private final Set<String>      handlers         = new HashSet<String>();
    private boolean                hasInitHandler;

    public ServiceModel(final Class<?> serviceClass, final IServiceProvider serviceBundle, final Services services) {
        this.serviceClass = serviceClass;
        this.serviceBundle = serviceBundle;
        this.streamService = services.getService(IStreamService.class);
        this.serviceId = serviceClass.getAnnotation(Service.class).id();
        this.findHandlers(serviceClass);
    }

    public String getName() {
        return this.serviceId;
    }

    @Override
    public Class<?> getServiceClass() {
        return this.serviceClass;
    }

    @Override
    public IServiceProvider getServiceProvider() {
        return this.serviceBundle;
    }

    @Override
    public IDialogHandler getHandlerFor(final Object instance, final IRequest request) throws Exception {
        IDialogHandler handler = null;
        final IMessage message = request.getMessage();
        final MessageKind messageKind = message.getMessageKind();
        switch (messageKind) {
            case RPC:
                final RPCMessage rpcMessage = (RPCMessage) message;
                handler = this.getRpcHandler(rpcMessage, instance);
                break;
            case SIMPLE:
                if (!this.hasInitHandler) {
                    throw new RuntimeException(this.serviceClass.getName() + " has no initial handlers !");
                }
                handler = ((IHandlerProvider) instance).initialHandler(request);
                break;
            default:
                throw new InvalidParameterException("Invalid message kind [" + messageKind + "] !");
        }
        return handler;
    }

    private IDialogHandler getRpcHandler(final RPCMessage message, final Object instance) throws Exception {
        final String methodName = message.getMethodName();
        final Class<?>[] parameters = this.classesOf(message.getParameters());
        final String signature = this.createSignature(methodName, parameters);
        if (!this.handlers.contains(signature)) {
            throw new Exception("No handler could be found for method [" + methodName + "] with parameters [" + Arrays.toString(parameters) + "] !");
        }
        return new RpcDialogHandler(this.streamService, instance, message.getMethod());
    }

    private String createSignature(final String methodName, final Class<?>[] parameters) {
        final Class<?>[] params = parameters == null ? new Class<?>[0] : parameters;
        for (int i = 0; i < params.length; i++) {
            final Class<?> primitive = Primitives.primitiveTypeOf(params[i]);
            params[i] = (primitive == null) ? params[i] : primitive;
        }
        final String types = Arrays.toString(params);
        return methodName + "#" + types;
    }

    private void findHandlers(final Class<?> serviceClass) {
        this.hasInitHandler = IHandlerProvider.class.isAssignableFrom(serviceClass);
        for (final Method method : serviceClass.getDeclaredMethods()) {
            final Handler handler = method.getAnnotation(Handler.class);
            if (handler == null) {
                continue;
            }
            if (!Modifier.isPublic(method.getModifiers())) {
                throw new RuntimeException("Handler method [" + method.getName() + "] should be defined as public !");
            }
            final String sign = this.createSignature(method.getName(), method.getParameterTypes());
            this.handlers.add(sign);
        }
    }

    public Class<?>[] classesOf(final Object[] objects) {
        if (objects == null) {
            return null;
        }
        final List<Class<?>> paramTypes = new ArrayList<Class<?>>();
        for (final Object o : objects) {
            paramTypes.add(o.getClass());
        }
        return paramTypes.toArray(new Class<?>[objects.length]);
    }

    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public String toString() {
        return "ServiceModel [info=" + this.serviceBundle.getBundleInfo() + "]";
    }

}
