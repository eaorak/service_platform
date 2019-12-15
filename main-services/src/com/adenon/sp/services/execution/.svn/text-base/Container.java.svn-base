package com.adenon.sp.services.execution;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.common.UnitExecution;
import com.adenon.sp.kernel.dialog.Dialog;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.execution.IContainer;
import com.adenon.sp.kernel.execution.IRequest;
import com.adenon.sp.services.service.info.IServiceModel;
import com.adenon.sp.services.service.info.IServiceProvider;


public class Container implements IContainer, Runnable {

    protected Logger          logger = Logger.getLogger("Container");
    protected Dialog          dialog;
    //
    protected IServiceModel   serviceModel;
    protected Object          serviceInstance;
    private final IThreadPool pool;
    private boolean           orphan = true;

    public static Container createRegular(final IServiceModel serviceModel) throws Exception {
        final IServiceProvider serviceProvider = serviceModel.getServiceProvider();
        final IThreadPool pool = serviceProvider.getServicePool();
        return new Container(pool, serviceModel);
    }

    public static Container createOrphan(final IThreadPool pool) throws Exception {
        return new Container(pool);
    }

    private Container(final IThreadPool pool, final IServiceModel serviceModel) throws Exception {
        this(pool);
        this.orphan = false;
        final IServiceProvider serviceProvider = serviceModel.getServiceProvider();
        this.logger = serviceProvider.getBundleInfo().getLogInfo().getLogger();
        this.serviceInstance = serviceProvider.getServiceFactory().getServiceInstance(serviceModel.getServiceClass());
        this.serviceModel = serviceModel;
    }

    private Container(final IThreadPool pool) throws Exception {
        this.pool = pool;
    }

    @Override
    public final void processEvent(final IRequest request) throws Exception {
        this.dialog = (Dialog) request.getSession();
        IDialogHandler handler = this.dialog.getHandler();
        if (!this.orphan) {
            handler = (handler != null) ? handler : this.serviceModel.getHandlerFor(this.serviceInstance, request);
            this.dialog.setHandler(handler);
        }
        if (handler == null) {
            throw new InvalidParameterException("Handler can not be null (" + this.dialog + ") !");
        }
        this.pool.execute(this);
    }

    @Override
    public final void run() {
        try {
            Event event = null;
            while ((event = this.dialog.getNextEvent(Direction.TOWARDS_IN, Direction.Processor.EXECUTION)) != null) {
                try {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Executing [" + event + "] ..");
                    }
                    final IDialogHandler handler = event.getDialog().getHandler();
                    handler.execute(event);
                } finally {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Execution of [" + event + "] has completed.");
                    }
                    event.setState(UnitExecution.PROCESSED);
                }
            }
        } catch (final Throwable e) {
            this.logger.error("Error in service execution ! : " + e.getMessage(), e);
        }
    }

}
