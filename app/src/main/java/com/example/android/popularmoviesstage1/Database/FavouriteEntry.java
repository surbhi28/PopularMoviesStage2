package com.example.android.popularmoviesstage1.Database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favourite")
public class FavouriteEntry implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private String movieId;

    @ColumnInfo(name = "movie_title")
    private String movieTitle;

    @ColumnInfo(name = "plot")
    private String plot;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @ColumnInfo(name = "votes")
    private double votes;

    @ColumnInfo(name = "image_poster")
    private String imagePoster;

    @ColumnInfo(name = "image_back_drop")
    private String imageBackDrop;

    @Ignore
    public FavouriteEntry(String movieId, String movieTitle, String plot, String releaseDate, double votes, String imagePoster, String imageBackDrop) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.plot = plot;
        this.releaseDate = releaseDate;
        this.votes = votes;
        this.imagePoster = imagePoster;
        this.imageBackDrop = imageBackDrop;
    }

    public FavouriteEntry(int id, String movieId, String movieTitle, String plot, String releaseDate, double votes, String imagePoster, String imageBackDrop) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.plot = plot;
        this.releaseDate = releaseDate;
        this.votes = votes;
        this.imagePoster = imagePoster;
        this.imageBackDrop = imageBackDrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVotes() {
        return votes;
    }

    public void setVotes(double votes) {
        this.votes = votes;
    }

    public String getImagePoster() {
        return imagePoster;
    }

    public void setImagePoster(String imagePoster) {
        this.imagePoster = imagePoster;
    }

    public String getImageBackDrop() {
        return imageBackDrop;
    }

    public void setImageBackDrop(String imageBackDrop) {
        this.imageBackDrop = imageBackDrop;
    }

}
