package com.example.myproject1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyGalleryCubeAdapter extends ArrayAdapter<MyGalleryCube>
{

    public MyGalleryCubeAdapter(@NonNull Context context, @NonNull List<MyGalleryCube> MyGalleryCubeArrayList) {
        super(context, 0, MyGalleryCubeArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            // Layout Inflater inflates each item to be displayed in GridView.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_gridview_mygallery, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvItemSubject = convertView.findViewById(R.id.tv_gvItemSubject);
            viewHolder.ivItemDrawing = convertView.findViewById(R.id.iv_gvItemDrawing);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyGalleryCube myGalleryCube = getItem(position);

        if (myGalleryCube != null)
        {
            viewHolder.tvItemSubject.setText(myGalleryCube.getSubject());
            // Clear previous image to avoid reusing old views with incorrect images
            viewHolder.ivItemDrawing.setImageBitmap(null);
            // Before starting the image loading, set a placeholder
            viewHolder.ivItemDrawing.setImageResource(R.drawable.mainscreen_mygallery_loading_pic);


            // Log the subject and bitmap string to debug
            Log.d("MyGalleryCubeAdapter", "Loading image for subject: " + myGalleryCube.getSubject() + " with bitmap ID: " + myGalleryCube.getBitmap());

            // get the image from the Firebase Storage
            FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference();
            StorageReference imageRef = storageRef.child(myGalleryCube.getBitmap()+".png");
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Use the bytes to display the image
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    viewHolder.ivItemDrawing.setImageBitmap(bitmap);
                    Log.d("MyGalleryCubeAdapter", "Image loaded successfully for subject: " + myGalleryCube.getSubject());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    Log.d("MyGalleryCubeAdapter", "Failed to load image for subject: " + myGalleryCube.getSubject() + ". Error: " + exception.getMessage());
                }
            });
        }

        return convertView;
    }



    private static class ViewHolder {
        TextView tvItemSubject;
        ImageView ivItemDrawing;
    }



}

