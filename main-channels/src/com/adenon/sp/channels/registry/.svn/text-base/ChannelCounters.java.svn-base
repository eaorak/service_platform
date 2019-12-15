package com.adenon.sp.channels.registry;

import com.adenon.sp.kernel.event.Direction;
import com.adenon.sp.kernel.event.message.MessageType;
import com.adenon.sp.monitoring.counters.ICounter;
import com.adenon.sp.monitoring.counters.IMonitor;
import com.adenon.sp.monitoring.counters.ITpsCounter;


public class ChannelCounters {

    private final IMonitor  monitor;
    private final String    name;
    private ITpsCounter[][] counters = new ITpsCounter[Direction.values().length][MessageType.values().length];

    public ChannelCounters(IMonitor monitor,
                           String name) {
        this.monitor = monitor;
        this.name = name;
        this.createCounters();
    }

    public void createCounters() {
        ICounter enablerCounter = this.monitor.createTpsCounter(this.name);
        for (Direction direction : Direction.values()) {
            ICounter directionCounter = this.monitor.createTpsCounter(direction.getDirection(), enablerCounter);
            for (MessageType type : MessageType.values()) {
                this.counters[direction.ordinal()][type.ordinal()] = this.monitor.createTpsCounter(type.name(), directionCounter);
            }
        }
    }

    public ITpsCounter get(Direction direction,
                           MessageType type) {
        return this.counters[direction.ordinal()][type.ordinal()];
    }

}
