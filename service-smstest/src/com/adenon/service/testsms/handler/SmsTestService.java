package com.adenon.service.testsms.handler;

import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.execution.IRequest;
import com.adenon.sp.services.service.info.Service;


@Service(id = "SmsTestService")
public class SmsTestService {

    private final SmsHandler handler = new SmsHandler();


    public IDialogHandler initialHandler(IRequest request) {
        return this.handler;
    }


}
