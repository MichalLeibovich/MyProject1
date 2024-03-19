package com.example.myproject1;

import android.graphics.Bitmap;
import android.widget.RatingBar;

public class RatingArea
{
    private String bitmap;
    private String username;
    private float rating; // Field to hold the rating value

  //  private float ratingBar;



    public RatingArea(String bitmap, String username, float rating) {
        this.bitmap = bitmap;
        this.username = username;
        this.rating = rating;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    /*
        public RatingArea(Bitmap bitmap, String username, RatingBar ratingBar)
        {
            this.bitmap = bitmap;
            this.username = username;
            this.ratingBar = ratingBar;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    /*
    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

     */

    //    public float getRating() {
//        return rating;
//    }
//
//    public void setRating(float rating) {
//        this.rating = rating;
//    }

}
