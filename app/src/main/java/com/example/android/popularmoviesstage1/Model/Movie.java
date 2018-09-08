package com.example.android.popularmoviesstage1.Model;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private String plot;
    private String releaseDate;
    private double votes;
    private String imagePoster;
    private String imageBackDrop;
    private String id;

    public Movie() {

    }

    public Movie(double movieVotes, String movieTitle, String movieImagePoster, String movieImageBackDrop, String moviePlot, String movieDate, String movieId) {
        votes = movieVotes;
        title = movieTitle;
        imagePoster = movieImagePoster;
        imageBackDrop = movieImageBackDrop;
        plot = moviePlot;
        releaseDate = movieDate;
        id = movieId;

    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVotes() {
        return votes;
    }

    public String getImagePoster() {
        return imagePoster;
    }

    public String getImageBackDrop() {
        return imageBackDrop;
    }

    public String getId() {
        return id;
    }
}
