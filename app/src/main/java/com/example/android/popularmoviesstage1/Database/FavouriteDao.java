package com.example.android.popularmoviesstage1.Database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favourite")
    LiveData<List<FavouriteEntry>> getAllFavourite();

    @Insert
    void insertFav(FavouriteEntry favouriteEntry);

    @Query("SELECT * FROM favourite WHERE movie_id = :movieId")
    LiveData<FavouriteEntry> isFav(String movieId);

    @Query("DELETE FROM favourite WHERE movie_id = :movieId ")
    void deleteFav(String movieId);
}
