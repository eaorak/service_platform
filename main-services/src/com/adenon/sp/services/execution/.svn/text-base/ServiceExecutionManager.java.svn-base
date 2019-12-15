package com.adenon.sp.services.execution;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.task.pool.IScheduledPool;
import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.dialog.IDialogCache;
import com.adenon.sp.kernel.error.ErrorObject;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.event.message.MessageType;
import com.adenon.sp.kernel.execution.IContainer;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.services.ServicesErrors;
import com.adenon.sp.services.handler.DialogChecker;
import com.adenon.sp.services.service.IServiceRegistry;
import com.adenon.sp.services.service.info.IServiceExecution;
import com.adenon.sp.services.service.info.IServiceModel;


public class ServiceExecutionManager implements IServiceExecution {

    private static Logger          logger = Logger.getLogger(ServiceExecutionManager.class);
    private final IServiceRegistry serviceRegistry;
    private final IThreadPool      defaultPool;

    public ServiceExecutionManager(final IServiceRegistry serviceRegistry, final Services services) {
        this.serviceRegistry = serviceRegistry;
        final IExecuterManager executer = services.getService(IExecuterManager.class);
        this.defaultPool = executer.getDefaultPool("main-services");
        this.scheduleDialogChecker(services, executer);
    }

    private void scheduleDialogChecker(final Services services, final IExecuterManager executer) {
        final IScheduledPool scheduledPool = executer.getScheduledPool("main-services");
        final IDialogCache dialogCache = services.getService(IDialogCache.class);
        scheduledPool.scheduleWithFixedDelay(new DialogChecker(dialogCache, this.defaultPool), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public IError executeService(final Event event) throws Exception {
        IError error = null;
        if (logger.isDebugEnabled()) {
            logger.debug("Service execution request : " + event);
        }
        final IDialog dialog = event.getDialog();
        final MessageType type = event.getMessage().getType();
        final String serviceId = dialog.getEndpoint();
        IContainer container = dialog.getContainer();
        switch (type) {
            case BEGIN: // ----------------------------------------
                final IServiceModel serviceModel = this.serviceRegistry.getServiceModel(serviceId);
                if (serviceId == null) {
                    throw new InvalidParameterException("Error ! Service unique id is NULL for dialog with info : " + dialog + " !");
                }
                error = this.check(event, serviceModel);
                if (error != null) {
                    return error;
                }
                container = Container.createRegular(serviceModel);
                dialog.setContainer(container);
                break;
            case CONTINUE: // --------------------------------------
            case END:
                if (dialog.getHandler() == null) {
                    throw new InvalidParameterException("Handler and container can not be null for [" + type + "] events !");
                }
                if (container == null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("[ServiceExecutionManager][executeService] : Container is null for dialog "
                                    + dialog
                                    + ", creating an orphan container.");
                    }
                    container = Container.createOrphan(this.defaultPool);
                }
                break;
            default:
                throw new InvalidParameterException("Invalid message type [" + type + "] !");
        }
        try {
            container.processEvent(event);
        } catch (final Exception e) {
            // TODO-RFC : Create error object
            error = ErrorObject.create(ServicesErrors.EXECUTION_ERROR, e);
            logger.error("Error ! " + e.getMessage(), e);
        }
        return error;
    }

    private IError check(final Event event, final IServiceModel serviceModel) {
        if (serviceModel == null) {
            return ServicesErrors.newError(ServicesErrors.SEE_TPS_EXCEEDED, "Service model is null.");
        }
        // TODO : Possible checks :: Service box passive | Tps exceeded | No service model ....
        return null;
    }

}