package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmoviesstage1.Adapter.TrailerAdapter;
import com.example.android.popularmoviesstage1.Database.FavouriteDatabase;
import com.example.android.popularmoviesstage1.Database.FavouriteEntry;
import com.example.android.popularmoviesstage1.Model.Movie;
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
    @BindView(R.id.toggle_button)
    ToggleButton favouriteButton;

    private FavouriteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        movie = (Movie) bundle.getSerializable("movie");

        ButterKnife.bind(this);

        database = FavouriteDatabase.getInstance(getApplicationContext());

        textOriginal.setText(movie.getTitle());
        rating.setText(Double.toString(movie.getVotes()));

        String imageBackDrop = movie.getImageBackDrop();
        String image = NetworkUtils.buildImageUrl(imageBackDrop).toString();
        Picasso.with(this).load(image).placeholder(R.drawable.download).into(backDrop);
        releaseDate.setText(movie.getReleaseDate());
        plotSynopsis.setText(movie.getPlot());

        favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MovieDetails.this, "Marked As Favourite", Toast.LENGTH_SHORT).show();
                    saveFavouriteMovie();
                } else {
                    Toast.makeText(MovieDetails.this, "Unmarked As Favourite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrailer.setLayoutManager(layoutManager);
        loadTrailers();
    }

    private void saveFavouriteMovie() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        movie = (Movie) bundle.getSerializable("movie");

        String movieId = movie.getId();
        String title = movie.getTitle();
        String plot = movie.getPlot();
        String date = movie.getReleaseDate();
        double votes = movie.getVotes();
        String posterPath = movie.getImagePoster();
        String imageBackDrop = movie.getImageBackDrop();

        final FavouriteEntry favouriteEntry = new FavouriteEntry(movieId, title, plot, date, votes, posterPath, imageBackDrop);
        database.dao().insertFav(favouriteEntry);

    }

    private void loadTrailers() {

        String id = movie.getId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.TrailerApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.TrailerApi api = retrofit.create(Api.TrailerApi.class);

        Call<MovieTrailer> call = api.getTrailer(id, "");
        call.enqueue(new Callback<MovieTrailer>() {
            @Override
            public void onResponse(Call<MovieTrailer> call, Response<MovieTrailer> response) {
                if (response.body() != null) {

                    List<MovieTrailer> trailerList = response.body().getResults();
                    recyclerViewTrailer.setHasFixedSize(true);
                    recyclerViewTrailer.setAdapter(new TrailerAdapter(getApplicationContext(), trailerList));
                } else {
                    Toast.makeText(MovieDetails.this, "Error in Fetching , Check Logs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieTrailer> call, Throwable t) {
                Log.d(LOG_TAG, "Failure occured", t);
            }
        });
    }
}