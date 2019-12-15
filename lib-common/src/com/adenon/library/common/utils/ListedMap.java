package com.adenon.library.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;


public class ListedMap<K, V> extends HashMap<K, V> {

    private static final long       serialVersionUID = 1182774129096352492L;
    private final List<Entry<K, V>> list;
    private int                     currentIndex;

    public ListedMap() {
        this.list = new ArrayList<Entry<K, V>>();
    }

    @Override
    public V put(K key,
                 V value) {
        V retValue = super.put(key, value);
        Entry<K, V> newEntry = new Entry<K, V>();
        newEntry.setKey(key);
        newEntry.setValue(value);
        this.list.add(newEntry);
        return retValue;
    }

    @Override
    public V remove(Object key) {
        this.removeFromList((K) key);
        return super.remove(key);
    }


    public V getNext() {
        if (this.list.isEmpty()) {
            return null;
        }
        int index = this.getNextIndex();
        try {
            Entry<K, V> entry = this.list.get(index);
            return entry.getValue();
        } catch (Exception e) {
            try {
                Entry<K, V> entry = this.list.get(0);
                return entry.getValue();
            } catch (Exception e2) {
                return null;
            }

        }
    }

    private synchronized int getNextIndex() {
        this.currentIndex++;
        this.currentIndex = this.currentIndex % this.list.size();
        return this.currentIndex;
    }

    private synchronized void removeFromList(K key) {
        for (Entry<K, V> myEntry : this.list) {
            if (key == myEntry.getKey()) {
                this.list.remove(myEntry);
                return;
            }
        }
    }

    private class Entry<MK, MV> implements Comparable<MV> {

        private MK key;
        private MV value;

        public MK getKey() {
            return this.key;
        }

        public void setKey(MK key) {
            this.key = key;
        }

        public MV getValue() {
            return this.value;
        }

        public void setValue(MV value) {
            this.value = value;
        }

        @Override
        public int compareTo(MV o) {
            try {
                if (this.value instanceof Comparable) {
                    Comparable comparable = (Comparable) this.value;
                    return comparable.compareTo(o);
                }
            } catch (Exception e) {
            }
            return 0;
        }
    }

    public void sort() {
        Object[] a = this.list.toArray();
        Arrays.sort(a);
        ListIterator<Entry<K, V>> listIterator = this.list.listIterator();
        for (int j = 0; j < a.length; j++) {
            listIterator.next();
            listIterator.set((Entry<K, V>) a[j]);
        }
    }

}
