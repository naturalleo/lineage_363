package com.lineage.server.model;

public class L1ObjectAmount<T> {

    private final T _obj;

    private final long _amount;

    public L1ObjectAmount(final T obj, final long amount) {
        this._obj = obj;
        this._amount = amount;
    }

    public T getObject() {
        return this._obj;
    }

    public long getAmount() {
        return this._amount;
    }
}
