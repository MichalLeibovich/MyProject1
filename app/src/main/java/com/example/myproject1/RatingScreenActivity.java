package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RatingScreenActivity extends AppCompatActivity {

    RatingBar rtBar;

    //a list to store all the products
    List<RatingArea> ratingAreasList;
    RecyclerView recyclerViewAreas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);





        // getting the recycler view from xml
        recyclerViewAreas = (RecyclerView)findViewById(R.id.recyclerView_ratingArea);
        recyclerViewAreas.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // initializing the ratingAreasList
        ratingAreasList = new ArrayList<>();


        // the views I have in each rating area

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img);

        //binding MainActivity.java with activity_main.xml file
       // rtBar = (RatingBar) findViewById(R.id.ratingBar);
        //finding the specific RatingBar with its unique ID
        LayerDrawable stars=(LayerDrawable)rtBar.getProgressDrawable();
        //Use for changing the color of RatingBar
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);


        //RatingArea ra1 = new RatingArea(bitmap, "username", 0);
        RatingArea ra1 = new RatingArea(bitmap, "username", rtBar);




        //phase 2 - add to array list
        ratingAreasList = new ArrayList<RatingArea>();
        ratingAreasList.add(ra1);
        //creating recyclerview adapter
        RatingAreaAdapter adapter = new RatingAreaAdapter(this, ratingAreasList);
        recyclerViewAreas.setAdapter(adapter);

    }


//    public void Call(View v)
//    {
//        // This function is called when button is clicked.
//        // Display ratings, which is required to be converted into string first.
//        TextView t = (TextView)findViewById(R.id.textView2);
//        t.setText("You Rated :"+String.valueOf(rt.getRating()));
//    }



}