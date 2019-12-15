package com.adenon.sp.channels.api;

import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.IMessage;

public interface IApiMessaging {

    Event begin(IMessage message,
                DialogType type) throws Exception;

    Event begin(IMessage message,
                DialogType type,
                IDialogHandler handler) throws Exception;

    Event continuee(IDialog dialog,
                    IMessage message) throws Exception;

    Event end(IDialog dialog,
              IMessage message) throws Exception;

    @Deprecated
    void sendStatefulMessage(IMessage message,
                             IDialogHandler handler) throws Exception;

    @Deprecated
    void sendStatelessMessage(IMessage message) throws Exception;

    @Deprecated
    void sendStatelessMessage(IMessage message,
                              Class<?> returnService) throws Exception;


}
