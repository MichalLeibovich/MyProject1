package com.example.myproject1;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RatingAreaViewHolder extends RecyclerView.ViewHolder
{
    TextView tvUserName;
    RatingBar ratingBar;
    ImageView ivDrawing;

    public RatingAreaViewHolder(View itemView) {
        super(itemView);
        ratingBar = itemView.findViewById(R.id.ratingBar_drawingRate);
        tvUserName = itemView.findViewById(R.id.textView_userName);
        ivDrawing = itemView.findViewById(R.id.imageView_drawing);

    }
}
