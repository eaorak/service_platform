package com.adenon.sp.streams;

import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.execution.IExecution;

public interface IStreamService extends IExecution {

    void register(IEventProcessor processor);

    void register(IEventProcessor processor,
                  Direction direction,
                  Priority priority);

}
