package com.adenon.sp.services;

import com.adenon.sp.kernel.error.ErrorActions;
import com.adenon.sp.kernel.error.ErrorObject;
import com.adenon.sp.kernel.error.IBaseError;
import com.adenon.sp.kernel.error.IError;

public enum ServicesErrors implements IBaseError {

    SEE_TPS_EXCEEDED(2000, "SEE TPS Exceeded.", ErrorActions.RETRY), /**/
    EXECUTION_ERROR(2001, "Error occured in service execution.", ErrorActions.RETRY), /**/
    SERVICE_BOX_IS_PASSIVE(2002, "Service Box is passive.", ErrorActions.ABORT), /**/
    SERVICE_BOX_SUSPENDED(2003, "Service Box is suspended.", ErrorActions.RETRY), /**/
    SERVICE_BOX_QUEUE_FULL(2004, "Service Box queue is full.", ErrorActions.RETRY), /**/
    DIALOG_TIMEOUT(2005, "Dialog timed out.", ErrorActions.RETRY); //

    private int          errCode;
    private String       errDesc;
    private ErrorActions action;
    private IError       error;

    private ServicesErrors(int errCode,
                           String errDesc,
                           ErrorActions action) {
        this.errCode = errCode;
        this.errDesc = errDesc;
        this.action = action;
        this.error = ErrorObject.create(this, errDesc);
    }

    @Override
    public int code() {
        return this.errCode /*+ SEEConstants.BUNDLE_ID*/;
    }

    @Override
    public String description() {
        return this.errDesc;
    }

    @Override
    public ErrorActions action() {
        return this.action;
    }

    public IError error() {
        return this.error;
    }

    public static IError newError(ServicesErrors error,
                                  String cause) {
        return ErrorObject.create(error, cause);
    }

}
