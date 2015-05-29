package com.avsystem.mongows.dao.impl;

import com.avsystem.mongows.dao.ActorDao;
import com.avsystem.mongows.data.Actor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * @author MKej
 */
public class RelMongoActorDao implements ActorDao {
    private final DBCollection collection;

    public RelMongoActorDao(DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public void removeAll() {
        collection.remove(new BasicDBObject());
    }

    @Override
    public void save(Actor object) {
        BasicDBObject dbObject = new BasicDBObject("_id", object.getId())
                .append("fullName", object.getFullName());
        collection.save(dbObject);
    }

    @Override
    public Actor load(String id) {
        DBObject dbObject = collection.findOne(id);
        if (dbObject != null) {
            String fullName = (String) dbObject.get("fullName");
            Actor actor = new Actor();
            actor.setId(id);
            actor.setFullName(fullName);
            return actor;
        } else {
            return null;
        }
    }
}
