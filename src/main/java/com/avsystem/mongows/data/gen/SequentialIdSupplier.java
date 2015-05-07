package com.avsystem.mongows.data.gen;

import com.avsystem.mongows.data.DataObject;

import java.util.function.Supplier;

/**
 * Created by MKej
 */
public class SequentialIdSupplier<T extends DataObject> implements Supplier<T> {
    private final Supplier<? extends T> originalSupplier;

    private int currentId;

    public SequentialIdSupplier(Supplier<? extends T> originalSupplier) {
        this.originalSupplier = originalSupplier;
    }

    @Override
    public T get() {
        T object = originalSupplier.get();
        int id = currentId++;
        object.setId(String.valueOf(id));
        return object;
    }
}
