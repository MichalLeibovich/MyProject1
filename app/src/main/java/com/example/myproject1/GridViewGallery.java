package com.example.myproject1;

public class GridViewGallery
{
    private String bitmap;
    private String subject;

    public GridViewGallery(String bitmap, String subject) {
        this.bitmap = bitmap;
        this.subject = subject;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
