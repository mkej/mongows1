package com.avsystem.mongows.dao.impl;

import com.avsystem.mongows.dao.MovieDao;
import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MKej
 */
public class EmbeddedMovieDao implements MovieDao {
    private final DBCollection collection;

    public EmbeddedMovieDao(DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public void removeAll() {
        collection.remove(new BasicDBObject());
    }

    @Override
    public void save(Movie movie) {
        BasicDBObject movieDbObject = new BasicDBObject("_id", movie.getId())
                .append("title", movie.getTitle());
        List<DBObject> actorsDb = new ArrayList<>();
        for (Actor actor : movie.getActors()) {
            BasicDBObject actorDbObject = new BasicDBObject("_id", actor.getId())
                    .append("fullName", actor.getFullName());
            actorsDb.add(actorDbObject);
        }
        movieDbObject.put("actors", actorsDb);
        collection.save(movieDbObject);
    }

    @Override
    public Movie load(String id) {
        DBObject movieDbObject = collection.findOne(id);
        if (movieDbObject == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle((String) movieDbObject.get("title"));

        List<Actor> actors = new ArrayList<>();
        List<DBObject> dbActors = (List<DBObject>) movieDbObject.get("actors");
        for (DBObject dbActor : dbActors) {
            String actorId = (String) dbActor.get("_id");
            String fullName = (String) dbActor.get("fullName");
            Actor actor = new Actor();
            actor.setId(actorId);
            actor.setFullName(fullName);
            actors.add(actor);
        }
        movie.setActors(actors);
        return movie;
    }
}
