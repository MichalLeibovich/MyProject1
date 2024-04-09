package com.example.myproject1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RatingAreaAdapter extends RecyclerView.Adapter<RatingAreaAdapter.MyRatingAreaViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the rating areas in a list
    private List<RatingArea> ratingAreaList;


    //getting the context and product list with constructor
    public RatingAreaAdapter(Context mCtx, List<RatingArea> ratingAreaList) {
        this.mCtx = mCtx;
        this.ratingAreaList = ratingAreaList;
    }


    @Override
    public MyRatingAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_recyclerview_rating, null);
        //View view = inflater.inflate(R.layout.custom_recyclerview_rating, parent, false);
        return new MyRatingAreaViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyRatingAreaViewHolder holder, int position) {
        //getting the product of the specified position
        RatingArea ratingArea = ratingAreaList.get(position);

        //binding the data with the viewholder views
        holder.tvUserName.setText(ratingArea.getUsername());
        holder.ratingBar.setRating(ratingArea.getRating());

        //storage.getImage(holder.ivDrawing,ratingArea.getBitmap());
        //holder.ivDrawing.setImageBitmap(ratingArea.getBitmap());

        // get the image from the firebase storage
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imageRef = storageRef.child(ratingArea.getBitmap()+".png");

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.ivDrawing.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                Log.d("FIREBASE", "setting image from storage failed " + exception.getMessage());
            }
        });




        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b) // from user
                {
                    // position - index in the arraylist
                    // v - value of ratring chosen
                    ratingAreaList.get(position).setRating(v);


                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return ratingAreaList.size();
    }


    class MyRatingAreaViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        RatingBar ratingBar;
        ImageView ivDrawing;

        public MyRatingAreaViewHolder(View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar_drawingRate);
            tvUserName = itemView.findViewById(R.id.textView_userName);
            ivDrawing = itemView.findViewById(R.id.imageView_drawing);
        }

    }
}
