package com.example.myproject1;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayersNamesViewHolder extends RecyclerView.ViewHolder
{
    TextView playerName;

    public PlayersNamesViewHolder(View itemView) {
        super(itemView);
        playerName = itemView.findViewById(R.id.playersNameTextView);
    }
}
