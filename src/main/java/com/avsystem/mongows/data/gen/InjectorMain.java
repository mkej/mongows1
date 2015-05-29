package com.avsystem.mongows.data.gen;

import com.avsystem.mongows.dao.MovieDao;
import com.avsystem.mongows.dao.impl.FakeActorDao;
import com.avsystem.mongows.dao.impl.FakeMovieDao;
import com.avsystem.mongows.dao.impl.RelMongoActorDao;
import com.avsystem.mongows.dao.impl.RelMongoMovieDao;
import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MKej
 */
public class InjectorMain {
    public static Actor getActor(MediaCreditCast cast) {
        Actor actor = new Actor();
        actor.setId(String.valueOf(cast.getCastId()));
        actor.setFullName(cast.getName());
        return actor;
    }

    public static Movie getMovie(TheMovieDbApi api, MovieInfo movieInfo) throws MovieDbException {
        int movieId = movieInfo.getId();

        Movie movie = new Movie();
        movie.setId(String.valueOf(movieId));
        movie.setTitle(movieInfo.getTitle());

        List<Actor> actors = new ArrayList<>();
        for (MediaCreditCast cast : api.getMovieCredits(movieId).getCast()) {
            Actor actor = getActor(cast);
            actors.add(actor);
        }

        movie.setActors(actors);
        return movie;
    }

    public static void main(String[] args) throws MovieDbException {
        if (args.length < 1) {
            System.err.println("No API key given!");
            return;
        }

        MongoClient client = new MongoClient();

        DB db = client.getDB("warsztat");

        RelMongoActorDao actorDao = new RelMongoActorDao(db.getCollection("actors"));

        MovieDao movieDao = new RelMongoMovieDao(db, actorDao);

        TheMovieDbApi api = new TheMovieDbApi(args[0]);
        for (MovieInfo movieInfo : api.getPopularMovieList(0, "en").getResults()) {
            Movie movie = getMovie(api, movieInfo);
            System.out.println(movie);
            movieDao.save(movie);
        }
    }
}
