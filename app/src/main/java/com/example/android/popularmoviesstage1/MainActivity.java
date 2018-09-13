
package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    List<Movie> movies;
    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private FavouriteAdapter favouriteAdapter;
    private String SEARCH_QUERY = "popular";
    private FavouriteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.setTitle(getResources().getString(R.string.popular));
        mRecyclerView = findViewById(R.id.recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(movieAdapter);

        new MovieAsyncTask().execute(SEARCH_QUERY);

        database = FavouriteDatabase.getInstance(getApplicationContext());

    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Movie movie = movies.get(clickedItemIndex);
        Bundle mBundle = new Bundle();
        Intent intent = new Intent(this, MovieDetails.class);
        mBundle.putSerializable("movie", movie);
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
                movieAdapter.movieData(null);
                SEARCH_QUERY = "popular";
                new MovieAsyncTask().execute(SEARCH_QUERY);
                this.setTitle(getResources().getString(R.string.popular));
                break;
            case R.id.top_rated_movies:
                movieAdapter.movieData(null);
                SEARCH_QUERY = "top_rated";
                new MovieAsyncTask().execute(SEARCH_QUERY);
                this.setTitle(getResources().getString(R.string.top_rated_movies));
                break;
            case R.id.favourite_movies:
                favouriteMovies();
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void favouriteMovies() {

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(favouriteAdapter);
        List<FavouriteEntry> favList = database.dao().getAllFavourite();
        favouriteAdapter = new FavouriteAdapter(this, favList);
        favouriteAdapter.notifyDataSetChanged();
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
                movieAdapter.movieData(movies);
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



