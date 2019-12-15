package com.adenon.sp.services.service.handler;

import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.execution.IRequest;

public interface IHandlerProvider {

    IDialogHandler initialHandler(IRequest request);

}
