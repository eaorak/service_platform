package com.adenon.sp.services.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * ExpandCache caches specified objects and expands if necessary. It also puts new objects <br>
 * to spaces starting with the smallest one, that occures with removal of old objects.
 */
public class ExpandCache<T> {

    private static int EXPAND_SIZE = 3;
    private T[]        cache;
    private int        lastIndex   = 0;

    @SuppressWarnings("unchecked")
    public ExpandCache(final Class<?> type) {
        this.cache = (T[]) Array.newInstance(type, EXPAND_SIZE);
    }

    public synchronized int put(final T resource) {
        this.cache[this.lastIndex] = resource;
        this.findLastIndex();
        return this.lastIndex;
    }

    public T get(final int index) {
        return this.cache[index];
    }

    public synchronized T remove(final int index) {
        final T resource = this.cache[index];
        this.cache[index] = null;
        if (index < this.lastIndex) {
            this.lastIndex = index;
        }
        return resource;
    }

    private void findLastIndex() {
        int index = -1;
        for (int i = this.lastIndex + 1; i < this.cache.length; i++) {
            if (this.cache[i] == null) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            index = this.cache.length;
            this.cache = Arrays.copyOf(this.cache, this.cache.length + EXPAND_SIZE);
        }
        this.lastIndex = index;
    }

    public static void main(final String[] args) {
        final ExpandCache<Integer> cache = new ExpandCache<Integer>(Integer.class);
        cache.put(0);
        cache.put(0);
        cache.put(0);
        cache.put(0);
        cache.put(0);
        cache.remove(2);
        cache.remove(1);
        cache.put(0);
        cache.remove(2);
        cache.put(0);
        cache.put(0);
        cache.remove(3);
        cache.remove(4);
        cache.put(0);
        cache.put(0);
        cache.remove(2);
    }

}
