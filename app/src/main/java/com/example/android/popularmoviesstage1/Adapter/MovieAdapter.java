package com.example.android.popularmoviesstage1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.NetworkUtils;
import com.example.android.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private MovieAdapterOnClickHandler mOnClickListener;
    private List<Movie> mMovie;
    private Context context;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mOnClickListener = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForItem = R.layout.all_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForItem, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String imageString = mMovie.get(position).getImagePoster();
        String image = NetworkUtils.buildImageUrl(imageString).toString();
        Picasso.with(context).load(image).placeholder(R.drawable.download).into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        if (null == mMovie) return 0;
        return mMovie.size();
    }

    public void movieData(List<Movie> movies) {
        mMovie = movies;
        notifyDataSetChanged();

    }

    public interface MovieAdapterOnClickHandler {
        void onListItemClicked(int clickedItemIndex);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImage;

        public MovieViewHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onListItemClicked(adapterPosition);

        }
    }

}
