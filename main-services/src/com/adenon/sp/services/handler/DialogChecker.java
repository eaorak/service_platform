package com.adenon.sp.services.handler;

import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.dialog.IDialogCache;
import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.ErrorObject;
import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.services.ServicesErrors;


public class DialogChecker implements Runnable {

    private final IDialogCache cache;
    private final IThreadPool  pool;

    public DialogChecker(final IDialogCache cache, final IThreadPool pool) {
        this.cache = cache;
        this.pool = pool;
    }

    @Override
    public void run() {
        for (final IDialog dialog : this.cache.getDialogs()) {
            if (!dialog.isExpiredByNow()) {
                continue;
            }
            this.cache.removeDialog(dialog);
            final IDialogHandler handler = dialog.getHandler();
            if (handler == null) {
                continue;
            }
            final IError error = ErrorObject.create(ServicesErrors.DIALOG_TIMEOUT, "Dialog [" + dialog + "] timed out !");
            this.pool.execute(new DialogTerminator(handler, error));
        }
    }

}