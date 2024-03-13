package com.example.myproject1;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RatingAreaAdapter extends RecyclerView.Adapter<RatingAreaAdapter.ToyViewHolder>
{
    //this context we will use to inflate the layout

    private Context mCtx;

    //we are storing all the products in a list

    private List<RatingArea> productList;

    //getting the context and product list with constructor

    public ToyAdapter(Context mCtx, List<RatingArea> productList)

    {

        this.mCtx = mCtx;

        this.productList = productList;

    }



    @Override

    public ToyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflating and returning our view holder

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.custom_layout, null);

        return new ToyViewHolder(view);

    }



    @Override

    public void onBindViewHolder(ToyViewHolder holder, int position) {

        //getting the product of the specified position

        Toy product = productList.get(position);

        //binding the data with the viewholder views

        holder.tvTitle.setText(product.getTitle());

        holder.tvSubTitle.setText(product.getSubTitle());

        holder.tvPrice.setText(String.valueOf(product.getPrice()));

        holder.ivProduct.setImageBitmap(product.getBitmap());

    }

    @Override

    public int getItemCount() {

        return productList.size();

    }





}
