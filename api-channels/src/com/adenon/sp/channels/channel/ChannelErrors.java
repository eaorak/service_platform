package com.adenon.sp.channels.channel;

import com.adenon.sp.kernel.error.ErrorActions;
import com.adenon.sp.kernel.error.IBaseError;

public enum ChannelErrors implements IBaseError {

    MESSAGE_SEND_ERROR(100, "Message could not be sent.", ErrorActions.ABORT), //
    CONNECTION_NOT_EXIST(101, "Enabler connection not exist.", ErrorActions.RETRY), //
    OPERATION_TIMEOUT(102, "Operation timed out.", ErrorActions.RETRY), //
    SYSTEM_ERROR(103, "System error.", ErrorActions.ABORT); //

    private int          errCode;
    private String       errDesc;
    private ErrorActions action;

    private ChannelErrors(int errCode,
                          String errDesc,
                          ErrorActions action) {
        this.errCode = errCode;
        this.errDesc = errDesc;
        this.action = action;
    }

    @Override
    public int code() {
        return this.errCode;
    }

    @Override
    public String description() {
        return this.errDesc;
    }

    @Override
    public ErrorActions action() {
        return this.action;
    }

}
