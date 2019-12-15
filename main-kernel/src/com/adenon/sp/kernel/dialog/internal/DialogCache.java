package com.adenon.sp.kernel.dialog.internal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.adenon.sp.kernel.dialog.DialogType;
import com.adenon.sp.kernel.dialog.IDialog;
import com.adenon.sp.kernel.dialog.IDialogCache;


public class DialogCache implements IDialogCache {

    private final Map<String/*dialogId*/, IDialog> dialogMap = new ConcurrentHashMap<String, IDialog>();
    private static final DialogCache                INSTANCE  = new DialogCache();

    public static DialogCache getInstance() {
        return INSTANCE;
    }

    private DialogCache() {
    }

    @Override
    public IDialog getDialog(final String id) throws Exception {
        final IDialog dialog = this.dialogMap.get(id);
        if (null == dialog) {
            throw new Exception("Dialog with id [" + id + "] not found in dialog cache !");
        }
        return dialog;
    }

    @Override
    public void putDialog(final IDialog dialog) {
        if (dialog.getType() == DialogType.STATEFUL) {
            this.dialogMap.put(dialog.getId(), dialog);
        }
    }

    @Override
    public void removeDialog(final IDialog dialog) {
        this.dialogMap.remove(dialog.getId());
    }

    @Override
    public void clear() {
        this.dialogMap.clear();
    }

    @Override
    public Collection<IDialog> getDialogs() {
        return this.dialogMap.values();
    }

}