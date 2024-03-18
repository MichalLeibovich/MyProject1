package com.example.myproject1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RatingAreaAdapter extends RecyclerView.Adapter<RatingAreaAdapter.RatingAreaViewHolder>
{
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the rating areas in a list
    private List<RatingArea> ratingAreaList;


    //getting the context and product list with constructor
    public RatingAreaAdapter(Context mCtx, List<RatingArea> ratingAreaList)
    {
        this.mCtx = mCtx;
        this.ratingAreaList = ratingAreaList;

    }


    @Override
    public RatingAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.custom_recyclerview_rating, null);
        View view = inflater.inflate(R.layout.custom_recyclerview_rating, parent, false);
        return new RatingAreaViewHolder(view);
    }



    @Override
    public void onBindViewHolder(RatingAreaViewHolder holder, int position)
    {
        //getting the product of the specified position
        RatingArea ratingArea = ratingAreaList.get(position);

        //binding the data with the viewholder views
        holder.tvUserName.setText(ratingArea.getUsername());
        holder.ratingBar.setRating(ratingArea.getRating());
        holder.ivDrawing.setImageBitmap(ratingArea.getBitmap());
    }

    @Override
    public int getItemCount()
    {
        return ratingAreaList.size();
    }




}
