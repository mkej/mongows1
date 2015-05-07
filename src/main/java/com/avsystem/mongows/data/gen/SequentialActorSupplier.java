package com.avsystem.mongows.data.gen;

import com.avsystem.mongows.data.Actor;

import java.util.function.Supplier;

/**
 * Created by MKej
 */
public class SequentialActorSupplier implements Supplier<Actor> {
    private int currentId;

    @Override
    public Actor get() {
        String actorId = String.valueOf(currentId++);
        Actor actor = new Actor();
        actor.setId(actorId);
        actor.setFullName("Actor #" + actorId);
        return actor;
    }
}
