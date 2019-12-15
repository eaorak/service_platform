package com.adenon.library.common.utils;


public class Concurrent<T> {

    private T value;

    public Concurrent(T value) {
        this.value = value;

    }

    public synchronized T getValue() {
        return this.value;
    }

    public synchronized void setValue(T newValue) {
        this.value = newValue;
    }

}
