package com.example.myproject1;

public class RatingAreaViewHolder
{


    TextView tvPrice, tvTitle, tvSubTitle;

    ImageView ivProduct;

    public ToyViewHolder(View itemView) {

        super(itemView);

        tvPrice = itemView.findViewById(R.id.tvPrice);

        tvTitle = itemView.findViewById(R.id.tvTitle);

        tvSubTitle = itemView.findViewById(R.id.tvSubTitle);

        ivProduct = itemView.findViewById(R.id.ivProduct);

    }
}
