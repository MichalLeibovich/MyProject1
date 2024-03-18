package com.example.myproject1;

import android.graphics.Bitmap;
import android.widget.RatingBar;

public class RatingArea
{
    private Bitmap bitmap;
    private String username;
    private float rating; // Field to hold the rating value




    public RatingArea(Bitmap bitmap, String username, float rating) {
        this.bitmap = bitmap;
        this.username = username;
        this.rating = rating;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
