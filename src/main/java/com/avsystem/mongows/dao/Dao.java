package com.avsystem.mongows.dao;

import com.avsystem.mongows.data.DataObject;

/**
 * Created by MKej
 */
public interface Dao<T extends DataObject> {
    void removeAll();

    void save(T object);

    T load(String id);
}
