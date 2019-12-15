package com.adenon.sp.executer.task.pool;

import com.adenon.sp.executer.task.IManaged;

public interface IManagedInternal extends IManaged {

    long getDelta();

    String getTaskClass();

}
