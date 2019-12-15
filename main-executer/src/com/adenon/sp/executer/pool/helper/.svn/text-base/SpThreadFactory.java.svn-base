package com.adenon.sp.executer.pool.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.adenon.sp.executer.pool.IThreadPoolInternal;
import com.adenon.sp.executer.thread.IThread;
import com.adenon.sp.executer.thread.IThreadFactory;


public class SpThreadFactory implements IThreadFactory {

    AtomicInteger                  threadNumber = new AtomicInteger(0);
    ThreadGroup                    group;
    private String                 poolName;
    private Map<Integer, SpThread> threads      = new ConcurrentSkipListMap<Integer, SpThread>();
    private String                 newLine      = "";
    private IThreadPoolInternal    pool;
    private List<SpThread>         threadList   = new ArrayList<SpThread>();

    public SpThreadFactory(String poolName) {
        this.poolName = poolName;
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.newLine = System.getProperty("line.separator");
    }

    public void injectPool(IThreadPoolInternal pool) {
        this.pool = pool;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public void remove(SpThread spThread) {
        this.threads.remove(spThread);
    }

    @Override
    public Thread newThread(Runnable r) {
        int index = this.threadNumber.incrementAndGet();
        SpThread t = new SpThread(this.group, r, this.poolName, index, this);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        this.threads.put(index, t);
        return t;
    }

    @Override
    public List<? extends IThread> getThreads() {
        this.threadList.clear();
        this.threadList.addAll(this.threads.values());
        return this.threadList;
    }

    @Override
    public String getThreadInfo() {
        StringBuilder str = new StringBuilder();
        Set<Entry<Integer, SpThread>> entrySet = this.threads.entrySet();
        for (Entry<Integer, SpThread> entry : entrySet) {
            str.append(entry.getValue().getInfo()).append(this.newLine);
        }
        String result = str.toString();
        return result.equals("") ? "No thread found." : result;
    }

    @Override
    public String kill(int index) {
        SpThread spThread = this.threads.get(index);
        if (spThread == null) {
            return "No thread can be found with id: " + index;
        }
        spThread.interrupt();
        boolean success = this.pool.addNewThread(1);
        if (success) {
            return "Thread [" + spThread.getName() + "] has been interrupted and a new thread added instead.";
        } else {
            return "Thread interrupted but new thread CAN NOT be added !";
        }
    }

    @Override
    public String getStackTrace(int index) {
        SpThread spThread = this.threads.get(index);
        if (spThread == null) {
            return "No thread can be found with id: " + index;
        }
        StringBuilder str = new StringBuilder(">> Stack trace of ").append(spThread.getName()).append(" : ").append(this.newLine);
        for (StackTraceElement element : spThread.getStackTrace()) {
            str.append(element.toString()).append(this.newLine);
        }
        return str.toString();
    }

}
