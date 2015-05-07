package com.avsystem.mongows.data;

import java.util.List;

/**
 * Created by MKej
 */
public class Movie extends DataObject {
    private String title;
    private List<Actor> actors;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", actors=" + actors +
                '}';
    }
}
