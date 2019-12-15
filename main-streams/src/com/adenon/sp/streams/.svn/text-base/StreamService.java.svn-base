package com.adenon.sp.streams;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.TreeSet;

import com.adenon.sp.executer.IExecuterManager;
import com.adenon.sp.executer.IPoolConfig;
import com.adenon.sp.executer.task.pool.IThreadPool;
import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.Event;
import com.adenon.sp.kernel.osgi.Services;
import com.adenon.sp.streams.IEventProcessor;
import com.adenon.sp.streams.IStreamService;
import com.adenon.sp.streams.Priority;
import com.adenon.sp.streams.ProcessorWrapper;


public class StreamService implements IStreamService {

    @SuppressWarnings("unchecked")
    private final Set<ProcessorWrapper>[] processors = (Set<ProcessorWrapper>[]) Array.newInstance(Set.class, Direction.values().length);
    private IThreadPool                   streamPool;
    private final Services                services;

    public StreamService(Services services) {
        this.services = services;
        for (Direction direction : Direction.values()) {
            this.processors[direction.ordinal()] = new TreeSet<ProcessorWrapper>();
        }
        //
        IExecuterManager threadManager = services.getService(IExecuterManager.class);
        try {
            IPoolConfig config = threadManager.newConfig("core-streams", "STREAM_POOL");
            config.setCoreSize(50);
            config.setMaxSize(100);
            this.streamPool = threadManager.createPool(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(IEventProcessor processor) {
        this.register(processor, null, Priority.DEFAULT);
    }

    @Override
    public void register(IEventProcessor processor,
                         Direction direction,
                         Priority priority) {
        // Check only one FIRST and LAST exist
        ProcessorWrapper wrapper = new ProcessorWrapper(processor, direction, priority);
        Direction dir = wrapper.getDirection();
        boolean isNull = dir == null;
        boolean processIN = (dir == Direction.TOWARDS_IN) || isNull;
        boolean processOUT = (dir == Direction.TOWARDS_OUT) || isNull;
        if (processIN) {
            this.processors[Direction.TOWARDS_IN.ordinal()].add(wrapper);
        }
        if (processOUT) {
            this.processors[Direction.TOWARDS_OUT.ordinal()].add(wrapper);
        }
    }

    @Override
    public void execute(Event event) throws Exception {
        this.execute(event, true);
    }

    @Override
    public void execute(Event event,
                        boolean async) throws Exception {
        if (async) {
            StreamTask task = new StreamTask(this.services, event, this);
            this.streamPool.submit(task);
        } else {
            StreamTask.process(this.services, this, event);
        }
    }

    public Set<ProcessorWrapper> getProcessors(Direction direction) {
        return this.processors[direction.ordinal()];
    }

}
