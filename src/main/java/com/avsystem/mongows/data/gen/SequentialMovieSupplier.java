package com.avsystem.mongows.data.gen;

import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by MKej
 */
public class SequentialMovieSupplier implements Supplier<Movie> {
    private final Supplier<? extends Actor> actorSupplier;
    private final int actorsPerMovie;

    private int currentId;

    public SequentialMovieSupplier(Supplier<? extends Actor> actorSupplier, int actorsPerMovie) {
        this.actorSupplier = actorSupplier;
        this.actorsPerMovie = actorsPerMovie;
    }

    @Override
    public Movie get() {
        String movieId = String.valueOf(currentId++);
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("Movie #" + movieId);
        List<Actor> actors = new ArrayList<>(actorsPerMovie);
        for (int i = 0; i < actorsPerMovie; i++) {
            actors.add(actorSupplier.get());
        }
        movie.setActors(actors);
        return movie;
    }
}
