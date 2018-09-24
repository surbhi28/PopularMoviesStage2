package com.example.android.popularmoviesstage1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmoviesstage1.Database.FavouriteDatabase;
import com.example.android.popularmoviesstage1.Database.FavouriteEntry;

import java.util.List;

public class MainViewModal extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModal.class.getName();

    private LiveData<List<FavouriteEntry>> movies;

    public MainViewModal(@NonNull Application application) {
        super(application);
        FavouriteDatabase database = FavouriteDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Actively Retrieving movies from database ");
        movies = database.dao().getAllFavourite();
    }

    public LiveData<List<FavouriteEntry>> getMovies() {
        return movies;
    }
}
