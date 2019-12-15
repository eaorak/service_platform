package com.adenon.sp.service.test;

import com.adenon.sp.channel.test.api.ITestChannelProvider;
import com.adenon.sp.channel.test.api.TestReturnMessage;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.execution.IRequest;
import com.adenon.sp.services.service.handler.Handler;
import com.adenon.sp.services.service.handler.IHandlerProvider;
import com.adenon.sp.services.service.info.Service;


@Service(id = "TestService")
public class TestService implements IHandlerProvider {

    private final ITestChannelProvider testEnabler;

    public TestService(ITestChannelProvider testApi) {
        this.testEnabler = testApi;
    }

    public IDialogHandler initialHandler(IRequest request) {
        return new IDialogHandler() {

            @Override
            public void execute(IRequest request) throws Exception {
                TestReturnMessage message = new TestReturnMessage();
                message.setMessage("a message from service");
                TestService.this.testEnabler.sendMessage(message);
            }

            @Override
            public void terminate(IRequest request,
                                  IError e) {
            }

        };
    }

    @Handler
    public String hebele() throws Exception {
        return "Hebeleeee";
    }

    @Handler
    public String hubele(String hebe,
                         int hube) throws Exception {
        return "Hubele der ki :: " + hebe + hube;
    }

}
