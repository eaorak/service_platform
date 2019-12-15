package com.adenon.sp.services.handler;

import com.adenon.sp.kernel.dialog.IDialogHandler;
import com.adenon.sp.kernel.error.IError;

public class DialogTerminator implements Runnable {

    private final IDialogHandler handler;
    private final IError         error;

    public DialogTerminator(final IDialogHandler handler, final IError error) {
        this.handler = handler;
        this.error = error;
    }

    @Override
    public void run() {
        this.handler.terminate(null, this.error);
    }

}
