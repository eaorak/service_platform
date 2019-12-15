package com.adenon.sp.services;

import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.services.service.info.IServiceExecution;
import com.adenon.sp.streams.IEventProcessor;


public class ServicesEventProcessor implements IEventProcessor {

    private final IServiceExecution execution;

    public ServicesEventProcessor(final IServiceExecution execution) {
        this.execution = execution;
    }

    @Override
    public IError process(final Event event) throws Exception {
        return this.execution.executeService(event);
    }

    @Override
    public boolean failOnError() {
        return true;
    }

}
