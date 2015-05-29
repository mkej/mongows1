package com.avsystem.mongows.benchmark;

import com.avsystem.mongows.dao.ActorDao;
import com.avsystem.mongows.dao.MovieDao;
import com.avsystem.mongows.dao.impl.*;
import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;
import com.avsystem.mongows.data.gen.SequentialActorSupplier;
import com.avsystem.mongows.data.gen.SequentialMovieSupplier;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.function.Supplier;

/**
 * Created by MKej
 */
@State(Scope.Benchmark)
public class WriteBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(WriteBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(1)
                .build();
        new Runner(options).run();
    }

    @Param({"EMBEDDED"})
    private DaoType daoType;

    @Param({"100"})
    private int actorsPerMovie;

    private Supplier<Actor> actorSupplier;
    private Supplier<Movie> movieSupplier;

    private ActorDao actorDao;
    private MovieDao movieDao;

    @Setup
    public void setup() {
        if (daoType == DaoType.FAKE) {
            actorDao = new FakeActorDao();
            movieDao = new FakeMovieDao(actorDao);
        } else if (daoType == DaoType.REL) {
            MongoClient client = new MongoClient();
            DB db = client.getDB("warsztat");
            actorDao = new RelMongoActorDao(db.getCollection("actors"));
            movieDao = new RelMongoMovieDao(db, actorDao);
        } else if (daoType == DaoType.EMBEDDED) {
            MongoClient client = new MongoClient();
            DB db = client.getDB("warsztat");
            actorDao = new FakeActorDao();
            movieDao = new EmbeddedMovieDao(db.getCollection("embMovies"));
        } else {
            throw new IllegalStateException();
        }

        actorSupplier = new SequentialActorSupplier();
        movieSupplier = new SequentialMovieSupplier(actorSupplier, actorsPerMovie);
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        actorDao.removeAll();
        movieDao.removeAll();
    }

    @Benchmark
    public void actorSave() {
        actorDao.save(actorSupplier.get());
    }

    @Benchmark
    public void movieSave() {
        movieDao.save(movieSupplier.get());
    }
}
