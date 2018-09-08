package com.example.android.popularmoviesstage1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Model.MovieTrailer;
import com.example.android.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<MovieTrailer> trailers;
    private Context context;

    public TrailerAdapter(Context context, List<MovieTrailer> list) {
        context = context;
        trailers = list;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_trailer, viewGroup, false);
        return new TrailerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        String name = trailers.get(position).getName();
        holder.nameView.setText(name);
        Picasso.with(context).load(R.drawable.youtube_icon).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailers.get(position).getKey()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (trailers == null) return 0;
        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView imageView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.trailer_name);
            imageView = itemView.findViewById(R.id.trailer_image);
        }
    }
}
