package com.example.android.popularmoviesstage1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.Model.MovieReview;
import com.example.android.popularmoviesstage1.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    List<MovieReview> reviewList;
    Context context;

    public ReviewAdapter(Context context, List<MovieReview> list) {
        this.context = context;
        reviewList = list;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (reviewList == null) {
            holder.noReviewView.setVisibility(View.VISIBLE);
            holder.noReviewView.setText("NO REVIEW");
            holder.authorView.setVisibility(View.GONE);
            holder.contentView.setVisibility(View.GONE);
        } else {
            holder.noReviewView.setVisibility(View.GONE);
            holder.authorView.setVisibility(View.VISIBLE);
            holder.contentView.setVisibility(View.VISIBLE);
            String author = reviewList.get(position).getAuthor();
            holder.authorView.setText(author);
            String content = reviewList.get(position).getContent();
            holder.contentView.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        if (reviewList == null) return 0;
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorView;
        TextView contentView;
        TextView noReviewView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorView = itemView.findViewById(R.id.review_author);
            contentView = itemView.findViewById(R.id.review_content);
            noReviewView = itemView.findViewById(R.id.no_review);
        }
    }
}
