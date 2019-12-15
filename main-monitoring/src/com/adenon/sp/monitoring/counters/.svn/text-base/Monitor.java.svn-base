package com.adenon.sp.monitoring.counters;

import java.util.Hashtable;
import java.util.Map;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;
import com.adenon.sp.monitoring.MonitoringService;


@MBean(location = ConfigLocation.SYSTEM, subLocation = "monitoring=Monitoring,counters=Monitors", id = "name")
public class Monitor implements IMonitor {

    private final Map<String, Counter> counterMap = new Hashtable<String, Counter>();
    private String                     name;
    private MonitoringService          service;

    @Deprecated
    public Monitor() {
    }

    public Monitor(final String name,
                   final MonitoringService service) {
        this.name = name;
        this.service = service;
    }

    @Override
    public ICounter createCounter(final String key) {
        return this.createCounter(key, null, false);
    }

    @Override
    public ICounter createCounter(final String key,
                                  final ICounter parent) {
        return this.createCounter(key, parent, false);
    }

    @Override
    public ITpsCounter createTpsCounter(final String key) {
        return this.createTpsCounter(key, null);
    }

    @Override
    public ITpsCounter createTpsCounter(final String key,
                                        final ICounter parent) {
        final TpsCounter counter = (TpsCounter) this.createCounter(key, parent, true);
        this.service.addTpsCounter(counter);
        return counter;
    }

    private ICounter createCounter(final String key,
                                   final ICounter parent,
                                   final boolean tps) {
        final String uniqueKey = this.uniqueKeyFor(key, parent);
        if (this.counterMap.containsKey(uniqueKey)) {
            throw new RuntimeException("Monitor already has a counter named [" + uniqueKey + "] !");
        }
        try {
            final Counter counter = tps ? new TpsCounter(key, this, parent) : new Counter(key, this, parent);
            this.service.registerCounter(counter);
            this.counterMap.put(uniqueKey, counter);
            return counter;
        } catch (final Exception e) {
            throw new RuntimeException("Error while creating counter configuration for [" + uniqueKey + "] !", e);
        }
    }

    @Override
    public ICounter deleteCounter(final String uniqueKey) {
        final ICounter counter = this.counterMap.get(uniqueKey);
        if (counter != null) {
            if (counter.getParent() != null) {
                throw new RuntimeException("Sub counters [" + uniqueKey + "] can not be deleted !");
            }
            for (final Counter c : this.counterMap.values().toArray(new Counter[0])) {
                if (c.isSubCounterOf(uniqueKey)) {
                    this.counterMap.remove(c.getKey());
                }
            }
        }
        final Counter removed = this.counterMap.remove(uniqueKey);
        if (removed instanceof TpsCounter) {
            this.service.removeTpsCounter((TpsCounter) removed);
        }
        return removed;
    }

    @Operation(name = "Print statistics")
    public String printStatistics(@Parameter(name = "Counter key") final String counterKey,
                                  @Parameter(name = "Statistic type", desc = "Can be REGULAR or TPS") final String typeStr) {
        final Counter counter = this.counterMap.get(counterKey);
        if (counter == null) {
            return "No counter could be found with key [" + counterKey + "].";
        }
        CounterType type = CounterType.valueOf(typeStr);
        if (type == null) {
            return "Invalid statistics type ! Choices [" + CounterType.choices() + "]";
        }
        switch (type) {
            case REGULAR:
                return counter.getStats().visualize(20);
            case TPS:
                return ((TpsCounter) counter).getTpsStats().visualize(20);
            default:
                return "Invalid statistic type !";
        }
    }

    private String uniqueKeyFor(final String key,
                                final ICounter parent) {
        return parent != null ? (parent.getUniqueKey() + ICounter.SEP + key) : key;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ICounter[] allCounters() {
        return this.counterMap.values().toArray(new ICounter[0]);
    }

    @Override
    public String toString() {
        return "Monitor [name=" + this.name + "]";
    }


}
