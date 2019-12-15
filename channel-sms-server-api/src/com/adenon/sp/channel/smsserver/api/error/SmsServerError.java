package com.adenon.sp.channel.smsserver.api.error;

import com.adenon.sp.kernel.error.ErrorActions;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.message.IMessage;


public class SmsServerError implements IError {

    private final String       description;
    private final ErrorActions errorAction;
    private final String       cause;
    private final int          errorCode;
    private IMessage           message;

    public SmsServerError(String description,
                          ErrorActions errorAction,
                          String cause,
                          int errorCode) {
        this.description = description;
        this.errorAction = errorAction;
        this.cause = cause;
        this.errorCode = errorCode;
    }

    @Override
    public int code() {
        return this.errorCode;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public ErrorActions action() {
        return this.errorAction;
    }

    @Override
    public String cause() {
        return this.cause;
    }

    public IMessage getMessage() {
        return this.message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }

    @Override
    public String trace() {
        // VASTODO Auto-generated method stub
        return null;
    }


}
