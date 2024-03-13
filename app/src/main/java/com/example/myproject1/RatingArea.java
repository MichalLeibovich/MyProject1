package com.example.myproject1;

import android.graphics.Bitmap;

public class RatingArea
{
    private Bitmap bitmap;
    private String username;

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

    public RatingArea(Bitmap bitmap, String username) {
        this.bitmap = bitmap;
        this.username = username;
    }
}
