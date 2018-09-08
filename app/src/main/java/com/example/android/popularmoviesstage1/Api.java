package com.example.android.popularmoviesstage1;

import com.example.android.popularmoviesstage1.Model.MovieTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class Api {

    public interface TrailerApi {

        String BASE_URL = "http://api.themoviedb.org/3/movie/";

        @GET("{movie_id}/videos")
        Call<MovieTrailer> getTrailer(@Path("movie_id") String id, @Query("api_key") String apiKey);
    }
}
