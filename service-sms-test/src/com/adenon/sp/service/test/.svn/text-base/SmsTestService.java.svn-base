package com.adenon.sp.service.test;

import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.execution.IRequest;
import com.adenon.sp.services.service.handler.IHandlerProvider;
import com.adenon.sp.services.service.info.Service;


@Service(id = "SmsTestService")
public class SmsTestService implements IHandlerProvider {

    public SmsTestService() {
    }

    public IDialogHandler initialHandler(IRequest request) {
        return new SmsTestHandler();
    }

}
