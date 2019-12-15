package com.adenon.sp.services.service.info;

import java.io.Serializable;

import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.execution.IRequest;


public interface IServiceModel extends Serializable {

    IServiceProvider getServiceProvider();

    Class<?> getServiceClass();

    IDialogHandler getHandlerFor(final Object instance,
                                 IRequest request) throws Exception;

}
