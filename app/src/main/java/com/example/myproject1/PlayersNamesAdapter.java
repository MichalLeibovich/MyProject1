package com.example.myproject1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayersNamesAdapter extends RecyclerView.Adapter<PlayersNamesViewHolder>
{

    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<String> productList;


    //getting the context and product list with constructor
    public PlayersNamesAdapter(Context mCtx, List<String> productList)
    {
        this.mCtx = mCtx;
        this.productList = productList;
    }



    @Override
    public PlayersNamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_recyclerview_playerslist, null);
        return new PlayersNamesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PlayersNamesViewHolder holder, int position) {
        //getting the product of the specified position
        String name = productList.get(position);
        //binding the data with the viewholder views
        holder.playerName.setText(name);

        //holder.playerName.setText(product.getPlayerName());
    }

    @Override

    public int getItemCount()
    {
        return productList.size();
    }


}
