package com.avsystem.mongows.benchmark;

import com.avsystem.mongows.dao.ActorDao;
import com.avsystem.mongows.dao.MovieDao;
import com.avsystem.mongows.dao.impl.*;
import com.avsystem.mongows.data.Actor;
import com.avsystem.mongows.data.Movie;
import com.avsystem.mongows.data.gen.RandomSupplier;
import com.avsystem.mongows.data.gen.SequentialActorSupplier;
import com.avsystem.mongows.data.gen.SequentialMovieSupplier;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author MKej
 */
@State(Scope.Benchmark)
public class ReadBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ReadBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(1)
                .build();
        new Runner(options).run();
    }

    @Param({"EMBEDDED"})
    private DaoType daoType;

    @Param({"10", "100"})
    private int actorsPerMovie;

    @Param({"10000"})
    private int actorCount;

    @Param({"10000"})
    private int movieCount;

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

        actorDao.removeAll();
        movieDao.removeAll();

        List<Actor> actors = new ArrayList<>();
        SequentialActorSupplier actorSupplier = new SequentialActorSupplier();
        for (int i = 0; i < actorCount; i++) {
            Actor actor = actorSupplier.get();
            actorDao.save(actor);
            actors.add(actor);
        }

        RandomSupplier<Actor> randomActorSupplier = new RandomSupplier<>(actors, new Random(131));

        SequentialMovieSupplier movieSupplier = new SequentialMovieSupplier(randomActorSupplier, actorsPerMovie);
        for (int i = 0; i < movieCount; i++) {
            Movie movie = movieSupplier.get();
            movieDao.save(movie);
        }
    }

    @Benchmark
    public void actorLoad() {
        int actorId = ThreadLocalRandom.current().nextInt(actorCount);
        actorDao.load(String.valueOf(actorId));
    }

    @Benchmark
    public void movieLoad() {
        int movieId = ThreadLocalRandom.current().nextInt(movieCount);
        movieDao.load(String.valueOf(movieId));
    }
}
