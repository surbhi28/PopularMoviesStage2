
package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmoviesstage1.Adapter.FavouriteAdapter;
import com.example.android.popularmoviesstage1.Adapter.MovieAdapter;
import com.example.android.popularmoviesstage1.Database.FavouriteDatabase;
import com.example.android.popularmoviesstage1.Database.FavouriteEntry;
import com.example.android.popularmoviesstage1.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    public static final String LOG_TAG = MainActivity.class.getName();

    public static String SAVED_RECYCLER_STATE = "recycler_state";
    List<Movie> movies;
    List<FavouriteEntry> FavList;
    Parcelable listState;
    LinearLayoutManager layoutManager;
    FavouriteAdapter favouriteAdapter;
    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private String SEARCH_QUERY = "popular";
    private FavouriteDatabase database;

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        layoutManager = new GridLayoutManager(this, calculateNoOfColumns(getApplicationContext()));
        mRecyclerView.setLayoutManager(layoutManager);
        database = FavouriteDatabase.getInstance(getApplicationContext());

    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        listState = layoutManager.onSaveInstanceState();
        state.putParcelable(SAVED_RECYCLER_STATE, listState);
        Log.d(LOG_TAG, " is onSave");
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null) {
            listState = state.getParcelable(SAVED_RECYCLER_STATE);
            Log.d(LOG_TAG, "Inside onRestore ");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SEARCH_QUERY == "popular") {
            this.setTitle(getResources().getString(R.string.popular));
        }
        if (SEARCH_QUERY == "top_rated") {
            this.setTitle(getResources().getString(R.string.top_rated_movies));
        }

        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
            if (SEARCH_QUERY == "") {
                mRecyclerView.setAdapter(new FavouriteAdapter(this, FavList));
                this.setTitle(getResources().getString(R.string.favourite));
            } else {
                MovieAdapter movieAdapter1 = new MovieAdapter(this);
                mRecyclerView.setAdapter(movieAdapter1);
                movieAdapter1.movieData(movies, this);
            }
        } else {

            movieAdapter = new MovieAdapter(this);
            mRecyclerView.setAdapter(movieAdapter);

            new MovieAsyncTask().execute(SEARCH_QUERY);
        }
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Movie movie = movies.get(clickedItemIndex);
        Bundle mBundle = new Bundle();
        Intent intent = new Intent(this, MovieDetails.class);
        mBundle.putParcelable("movie", movie);
        intent.putExtra("BUNDLE", mBundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                movieAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(movieAdapter);
                movieAdapter.movieData(null, this);
                SEARCH_QUERY = "popular";
                new MovieAsyncTask().execute(SEARCH_QUERY);
                this.setTitle(getResources().getString(R.string.popular));
                break;
            case R.id.top_rated_movies:
                movieAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(movieAdapter);
                movieAdapter.movieData(null, this);
                SEARCH_QUERY = "top_rated";
                new MovieAsyncTask().execute(SEARCH_QUERY);
                this.setTitle(getResources().getString(R.string.top_rated_movies));
                break;
            case R.id.favourite_movies:
                SEARCH_QUERY = "";
                favouriteMovies();
                this.setTitle(getResources().getString(R.string.favourite));
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void favouriteMovies() {
        favouriteAdapter = new FavouriteAdapter(getApplicationContext());
        mRecyclerView.setAdapter(favouriteAdapter);

        MainViewModal viewModal = ViewModelProviders.of(this).get(MainViewModal.class);
        viewModal.getMovies().observe(this, new Observer<List<FavouriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteEntry> favouriteEntries) {
                FavList = favouriteEntries;
                Log.d(LOG_TAG, "Updating List of favourite movies from LiveData in ViewModal");
                favouriteAdapter.saveFavourite(favouriteEntries);
            }
        });
    }

    public class MovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... strings) {

            URL url = NetworkUtils.buildUrl(strings[0]);
            String jsonResponse = "";
            try {
                jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            movies = getMoviesFromJson(jsonResponse);
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                movieAdapter.movieData(movies, getApplicationContext());
            }
        }

        public List<Movie> getMoviesFromJson(String movieJson) {
            if (movieJson.isEmpty()) {
                return null;
            }
            List<Movie> movies1 = new ArrayList<>();
            try {
                JSONObject rootObject = new JSONObject(movieJson);
                JSONArray array = rootObject.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int id = object.getInt("id");
                    Double votes = object.getDouble("vote_average");
                    String title = object.getString("title");
                    String posterPath = object.getString("poster_path");
                    String backDropPath = object.getString("backdrop_path");
                    String overview = object.getString("overview");
                    String date = object.getString("release_date");

                    String idString = String.valueOf(id);

                    Movie movie = new Movie(votes, title, posterPath, backDropPath, overview, date, idString);
                    movies1.add(movie);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the JSON results", e);
            }
            return movies1;

        }
    }
}



