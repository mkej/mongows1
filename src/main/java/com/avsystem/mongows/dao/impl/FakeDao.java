package com.avsystem.mongows.dao.impl;

import com.avsystem.mongows.dao.Dao;
import com.avsystem.mongows.data.DataObject;

import java.util.HashMap;

/**
 * Created by MKej
 */
public class FakeDao<T extends DataObject> implements Dao<T> {
    public static final int MAX_SIZE = 10000;
    private final HashMap<String, T> objects = new HashMap<>();

    @Override
    public void removeAll() {
        objects.clear();
    }

    @Override
    public void save(T object) {
        if (objects.size() >= MAX_SIZE) {
            objects.remove(object.getId()); // to trigger hash counting
            return;
        }
        objects.put(object.getId(), object);
    }

    @Override
    public T load(String id) {
        return objects.get(id);
    }
}
