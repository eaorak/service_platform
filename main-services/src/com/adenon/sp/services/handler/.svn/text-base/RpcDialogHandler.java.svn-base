package com.adenon.sp.services.handler;

import java.lang.reflect.Method;

import com.adenon.sp.kernel.dialog.Dialog;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.MessageType;
import com.adenon.sp.kernel.event.message.rpc.RPCMessage;
import com.adenon.sp.kernel.execution.IExecution;
import com.adenon.sp.kernel.execution.IRequest;
import com.adenon.sp.streams.IStreamService;


public class RpcDialogHandler implements IDialogHandler {

    private final IStreamService stream;
    private final Object         instance;
    private final Method         handler;

    public RpcDialogHandler(final IStreamService stream, final Object instance, final Method handler) {
        if ((stream == null) || (handler == null) || (instance == null)) {
            throw new RuntimeException("Parameters can not be null !");
        }
        this.stream = stream;
        this.instance = instance;
        this.handler = handler;
    }

    @Override
    public void execute(final IRequest request) throws Exception {
        final RPCMessage message = (RPCMessage) request.getMessage();
        message.setType(MessageType.END);
        IExecution returnInf = message.getReturn();
        if (returnInf == null) {
            returnInf = this.stream;
            message.setReturn(this.stream);
        }
        final Dialog dialog = (Dialog) request.getSession();
        final Event endEvent = dialog.end(message, Direction.TOWARDS_OUT);
        final Object result = this.handler.invoke(this.instance, message.getParameters());
        message.setReturnValue(result);
        returnInf.execute(endEvent);
    }

    @Override
    public void terminate(final IRequest request, final IError e) {
        // TODO : What to do ?
    }

}
