package com.example.android.popularmoviesstage1.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {FavouriteEntry.class}, version = 1, exportSchema = false)
public abstract class FavouriteDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavouriteDatabase.class.getName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favouritemovies";
    private static FavouriteDatabase sInstance;

    public static FavouriteDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating New Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouriteDatabase.class, FavouriteDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting Database Instance");
        return sInstance;
    }

    public abstract FavouriteDao dao();
}
