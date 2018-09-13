package com.example.android.popularmoviesstage1.Model;

import java.util.List;

public class MovieReview {

    private List<MovieReview> results;
    private String author;
    private String content;

    public List<MovieReview> getResults() {
        return results;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
