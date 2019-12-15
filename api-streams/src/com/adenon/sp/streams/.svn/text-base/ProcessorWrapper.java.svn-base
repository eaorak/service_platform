package com.adenon.sp.streams;

import com.adenon.sp.kernel.error.IError;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;

public class ProcessorWrapper implements IEventProcessor, Comparable<ProcessorWrapper> {

    private final IEventProcessor processor;
    private final Direction       direction;
    private final Priority        priority;

    public ProcessorWrapper(IEventProcessor processor,
                            Direction direction,
                            Priority priority) {
        this.processor = processor;
        this.direction = direction;
        this.priority = priority;
    }

    @Override
    public IError process(Event event) throws Exception {
        return this.processor.process(event);
    }

    @Override
    public boolean failOnError() {
        return this.processor.failOnError();
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public int compareTo(ProcessorWrapper o) {
        return this.priority.value() - o.getPriority().value();
    }

}
