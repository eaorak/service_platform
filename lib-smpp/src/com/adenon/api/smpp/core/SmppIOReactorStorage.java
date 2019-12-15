package com.adenon.api.smpp.core;

import java.util.ArrayList;


public class SmppIOReactorStorage {

    // TODO: This class should be thread safe

    private final ArrayList<IIOReactor> ioReactors = new ArrayList<IIOReactor>();

    public SmppIOReactorStorage() {
    }

    public void addSmppIOReactor(final IIOReactor ioReactor) {
        this.getIoReactors().add(ioReactor);
    }

    public int size() {
        return this.getIoReactors().size();
    }

    public IIOReactor get(final int index) {
        return this.getIoReactors().get(index);
    }

    public void remove(final IIOReactor ioReactor) {
        this.getIoReactors().remove(ioReactor);
    }

    public ArrayList<IIOReactor> getIoReactors() {
        return this.ioReactors;
    }
}
