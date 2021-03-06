package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Adapter.ReviewAdapter;
import com.example.android.popularmoviesstage1.Adapter.TrailerAdapter;
import com.example.android.popularmoviesstage1.Database.FavouriteDatabase;
import com.example.android.popularmoviesstage1.Database.FavouriteEntry;
import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.Model.MovieReview;
import com.example.android.popularmoviesstage1.Model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    public static final String LOG_TAG = MovieDetails.class.getName();

    Movie movie;
    @BindView(R.id.backdrop_image)
    ImageView backDrop;
    @BindView(R.id.text_original)
    TextView textOriginal;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.text_release_date)
    TextView releaseDate;
    @BindView(R.id.text_plot_synopsis)
    TextView plotSynopsis;
    @BindView(R.id.recycler_view1)
    RecyclerView recyclerViewTrailer;
    @BindView(R.id.button)
    Button favouriteButton;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerViewReview;

    private FavouriteDatabase database;
    Boolean favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        movie = (Movie) bundle.getParcelable("movie");

        ButterKnife.bind(this);

        database = FavouriteDatabase.getInstance(getApplicationContext());

        checkIfFavourite();

        textOriginal.setText(movie.getTitle());
        rating.setText(Double.toString(movie.getVotes()));

        String imageBackDrop = movie.getImageBackDrop();
        String image = NetworkUtils.buildImageUrl(imageBackDrop).toString();
        Picasso.with(this).load(image).placeholder(R.drawable.download).into(backDrop);
        releaseDate.setText(movie.getReleaseDate());
        plotSynopsis.setText(movie.getPlot());

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favourite) {
                    deleteFavouriteMovie();
                    favouriteButton.setBackground(ContextCompat.getDrawable(MovieDetails.this, R.drawable.ic_favorite_off));
                    Toast.makeText(MovieDetails.this, "Unmarked As Favourite", Toast.LENGTH_SHORT).show();
                } else {
                    saveFavouriteMovie();
                    favouriteButton.setBackground(ContextCompat.getDrawable(MovieDetails.this, R.drawable.ic_favorite_on));
                    Toast.makeText(MovieDetails.this, "Marked As Favourite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrailer.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL);
        recyclerViewTrailer.addItemDecoration(decoration);
        loadTrailers();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerViewReview.setLayoutManager(layoutManager1);
        DividerItemDecoration decoration1 = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerViewReview.addItemDecoration(decoration1);
        loadReviews();
    }

    private void checkIfFavourite() {
        final String movieId = movie.getId();

        final LiveData<FavouriteEntry> list = database.dao().isFav(movieId);
        list.observe(this, new Observer<FavouriteEntry>() {
            @Override
            public void onChanged(@Nullable FavouriteEntry favouriteEntry) {
                list.removeObserver(this);
                if (favouriteEntry == null) {
                    favourite = false;
                    favouriteButton.setBackground(ContextCompat.getDrawable(MovieDetails.this, R.drawable.ic_favorite_off));
                } else {
                    favourite = true;
                    favouriteButton.setBackground(ContextCompat.getDrawable(MovieDetails.this, R.drawable.ic_favorite_on));


                }
            }
        });
    }

    private void saveFavouriteMovie() {

        String movieId = movie.getId();
        String title = movie.getTitle();
        String plot = movie.getPlot();
        String releaseDate = movie.getReleaseDate();
        double votes = movie.getVotes();
        String posterPath = movie.getImagePoster();
        String imageBackDrop = movie.getImageBackDrop();

        final FavouriteEntry favouriteEntry = new FavouriteEntry(movieId, title, plot, releaseDate, votes, posterPath, imageBackDrop);
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.dao().insertFav(favouriteEntry);
                Log.d(LOG_TAG, "movie saved" + favouriteEntry.getMovieTitle());
            }
        });

    }

    private void deleteFavouriteMovie() {
        final String movieId = movie.getId();
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.dao().deleteFav(movieId);
                Log.d(LOG_TAG, "Movie Deleted " + movieId);
            }
        });
    }

    private void loadTrailers() {

        String id = movie.getId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.MovieApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.MovieApi api = retrofit.create(Api.MovieApi.class);

        Call<MovieTrailer> call = api.getTrailer(id, BuildConfig.My_Api_Key);
        call.enqueue(new Callback<MovieTrailer>() {
            @Override
            public void onResponse(Call<MovieTrailer> call, Response<MovieTrailer> response) {
                if (response.body() != null) {

                    List<MovieTrailer> trailerList = response.body().getResults();
                    recyclerViewTrailer.setHasFixedSize(true);
                    recyclerViewTrailer.setAdapter(new TrailerAdapter(getApplicationContext(), trailerList));
                } else {
                    Toast.makeText(MovieDetails.this, "Error in Fetching Trailers , Check Logs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieTrailer> call, Throwable t) {
                Log.d(LOG_TAG, "Failure occured", t);
            }
        });
    }

    private void loadReviews() {

        String id = movie.getId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.MovieApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.MovieApi api = retrofit.create(Api.MovieApi.class);

        Call<MovieReview> call = api.getReview(id, BuildConfig.My_Api_Key);
        call.enqueue(new Callback<MovieReview>() {
            @Override
            public void onResponse(Call<MovieReview> call, Response<MovieReview> response) {
                if (response.body() != null) {
                    List<MovieReview> reviewList = response.body().getResults();
                    recyclerViewReview.setAdapter(new ReviewAdapter(getApplicationContext(), reviewList));
                } else {
                    Toast.makeText(MovieDetails.this, "Error in Fetching Reviews , Check Logs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieReview> call, Throwable t) {
                Log.d(LOG_TAG, "Failure occured", t);
            }
        });

    }
}
