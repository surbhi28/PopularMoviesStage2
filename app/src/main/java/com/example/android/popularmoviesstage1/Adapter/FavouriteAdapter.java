package com.example.android.popularmoviesstage1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.Database.FavouriteEntry;
import com.example.android.popularmoviesstage1.FavouriteDetails;
import com.example.android.popularmoviesstage1.NetworkUtils;
import com.example.android.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    public static final String LOG_TAG = FavouriteAdapter.class.getName();

    List<FavouriteEntry> favouriteList;
    Context context;

    public FavouriteAdapter(Context context) {
        this.context = context;
    }

    public FavouriteAdapter(Context context, List<FavouriteEntry> FavList) {
        this.context = context;
        favouriteList = FavList;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.all_movie, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, final int position) {
        String imagePoster = favouriteList.get(position).getImagePoster();
        String image = NetworkUtils.buildImageUrl(imagePoster).toString();
        Log.d(LOG_TAG, "image string is " + image);
        Picasso.with(context).load(image).placeholder(R.drawable.download).into(holder.favImage);
        holder.favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteEntry favouriteEntry = favouriteList.get(position);
                Bundle mBundle = new Bundle();
                Intent intent = new Intent(context, FavouriteDetails.class);
                mBundle.putSerializable("favourite", favouriteEntry);
                intent.putExtra("BUNDLE", mBundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (favouriteList == null) {
            return 0;
        }
        return favouriteList.size();
    }


    public void saveFavourite(List<FavouriteEntry> favList) {
        favouriteList = favList;

    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView favImage;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
