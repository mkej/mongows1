package com.avsystem.mongows.dao.impl;

import com.avsystem.mongows.dao.ActorDao;
import com.avsystem.mongows.dao.MovieDao;
import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MKej
 */
public class RelMongoMovieDao implements MovieDao {
    private final DBCollection movies;
    private final DBCollection moviesToActors;
    private final ActorDao actorDao;

    public RelMongoMovieDao(DB db, ActorDao actorDao) {
        this.actorDao = actorDao;
        movies = db.getCollection("movies");
        moviesToActors = db.getCollection("movies_to_actors");
    }

    @Override
    public void removeAll() {
        movies.remove(new BasicDBObject());
        moviesToActors.remove(new BasicDBObject());
    }

    @Override
    public void save(Movie movie) {
        BasicDBObject movieDbObject = new BasicDBObject("_id", movie.getId())
                .append("title", movie.getTitle());
        movies.save(movieDbObject);
        for (Actor actor : movie.getActors()) {
            BasicDBObject relDbObject = new BasicDBObject("movieId", movie.getId())
                    .append("actorId", actor.getId());
            moviesToActors.save(relDbObject);
            actorDao.save(actor);
        }
    }

    @Override
    public Movie load(String id) {
        DBObject movieDbObject = movies.findOne(id);
        if (movieDbObject == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle((String) movieDbObject.get("title"));

        DBCursor cursor = moviesToActors.find(new BasicDBObject("movieId", id));
        List<Actor> actors = new ArrayList<>();
        for (DBObject relDbObject : cursor) {
            String actorId = (String) relDbObject.get("actorId");
            Actor actor = actorDao.load(actorId);
            if (actor != null) {
                actors.add(actor);
            }
        }
        movie.setActors(actors);
        return movie;
    }
}
