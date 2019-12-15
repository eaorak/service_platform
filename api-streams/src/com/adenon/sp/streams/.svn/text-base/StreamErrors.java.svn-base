package com.adenon.sp.streams;

import com.adenon.sp.kernel.error.ErrorActions;
import com.adenon.sp.kernel.error.IBaseError;


public enum StreamErrors implements IBaseError {

    STREAM_PROCESS_ERROR(100, "Stream could not be processed..", ErrorActions.ABORT);

    private int          errCode;
    private String       errDesc;
    private ErrorActions action;

    private StreamErrors(int errCode,
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
