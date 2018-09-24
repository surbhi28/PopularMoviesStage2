package com.example.android.popularmoviesstage1.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

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

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

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


    public Movie(Parcel source) {
        votes = source.readDouble();
        title = source.readString();
        imagePoster = source.readString();
        imageBackDrop = source.readString();
        plot = source.readString();
        releaseDate = source.readString();
        id = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(votes);
        parcel.writeString(title);
        parcel.writeString(imagePoster);
        parcel.writeString(imageBackDrop);
        parcel.writeString(plot);
        parcel.writeString(releaseDate);
        parcel.writeString(id);

    }
}
