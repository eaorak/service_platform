package com.adenon.sp.channels.messaging;

import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.IMessage;

public interface IChannelMessaging {

    Event begin(IMessage eventMessage,
                DialogType status,
                long lifeTime);

    Event continuee(IDialog dialog,
                    IMessage eventMessage);

    Event end(IDialog dialog,
              IMessage eventMessage);

}
