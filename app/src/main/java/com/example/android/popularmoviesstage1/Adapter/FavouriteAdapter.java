package com.example.android.popularmoviesstage1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.Database.FavouriteEntry;
import com.example.android.popularmoviesstage1.NetworkUtils;
import com.example.android.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    public static final String LOG_TAG = FavouriteAdapter.class.getName();

    List<FavouriteEntry> favouriteList;
    Context context;

    public FavouriteAdapter(Context context, List<FavouriteEntry> favList) {
        this.context = context;
        favouriteList = favList;

    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.all_movie, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        String imagePoster = favouriteList.get(position).getImagePoster();
        String image = NetworkUtils.buildImageUrl(imagePoster).toString();
        Log.d(LOG_TAG, "image string is " + image);
        Picasso.with(context).load(image).placeholder(R.drawable.download).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (favouriteList == null) {
            return 0;
        }
        return favouriteList.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.image);
        }
    }
}
