package com.avsystem.mongows.data.gen;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by MKej
 */
public class RandomSupplier<T> implements Supplier<T> {
    private final List<? extends T> list;
    private final Random random;

    public RandomSupplier(List<? extends T> list, Random random) {
        this.list = list;
        this.random = random;
    }

    @Override
    public T get() {
        return list.get(random.nextInt(list.size()));
    }
}
