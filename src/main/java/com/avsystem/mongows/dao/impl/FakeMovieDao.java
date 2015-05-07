package com.avsystem.mongows.dao.impl;

import com.avsystem.mongows.dao.ActorDao;
import com.avsystem.mongows.dao.MovieDao;
import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;

/**
 * Created by MKej
 */
public class FakeMovieDao extends FakeDao<Movie> implements MovieDao {
    private final ActorDao actorDao;

    public FakeMovieDao(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    @Override
    public void save(Movie object) {
        super.save(object);
        for (Actor actor : object.getActors()) {
            actorDao.save(actor);
        }
    }

    @Override
    public Movie load(String id) {
        Movie movie = super.load(id);
        for (Actor actor : movie.getActors()) {
            actorDao.load(actor.getId());
        }
        return movie;
    }
}
