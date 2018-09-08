package com.example.android.popularmoviesstage1.Model;

import java.util.List;

public class MovieTrailer {

    private List<MovieTrailer> results;
    private String id;
    private String key;
    private String name;

    public List<MovieTrailer> getResults() {
        return results;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
