package com.adenon.library.common.request.response;

import com.adenon.sp.kernel.dialog.IDialog;


public class Container<Request, Response> {

    public static final int ERROR_RESPONSE_EXPIRED      = 1;
    public static final int ERROR_RESPONSE_ISNULL       = 2;
    public static final int ERROR_RESPONSE_DIALOG_EMPTY = 3;

    private Object          waitObject;
    private Request         requestObject;
    private Response        responseObject;
    private boolean         errorOccured;
    private String          errorDescription;
    private int             errorCode;
    private boolean         responseReceived;
    private IDialog         dialog;

    public Object getWaitObject() {
        return this.waitObject;
    }

    public void setWaitObject(Object waitObject) {
        this.waitObject = waitObject;
    }

    public Request getRequestObject() {
        return this.requestObject;
    }

    public void setRequestObject(Request requestObject) {
        this.requestObject = requestObject;
    }

    public Response getResponseObject() {
        return this.responseObject;
    }

    public void setResponseObject(Response responseObject) {
        this.responseObject = responseObject;
    }


    public boolean isErrorOccured() {
        return this.errorOccured;
    }


    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }


    public String getErrorDescription() {
        return this.errorDescription;
    }


    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }


    public int getErrorCode() {
        return this.errorCode;
    }


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isResponseReceived() {
        return this.responseReceived;
    }

    public void setResponseReceived(boolean responseReceived) {
        this.responseReceived = responseReceived;
    }

    public IDialog getDialog() {
        return this.dialog;
    }

    public void setDialog(IDialog dialog) {
        this.dialog = dialog;
    }


}
