package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RatingScreenActivity extends AppCompatActivity {

    RatingBar rtBar;

    //a list to store all the products
    List<RatingArea> ratingAreasList;
    RecyclerView recyclerViewAreas;

    String gameId;

    GameRoom gameRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);



        gameId = getIntent().getStringExtra("gameId");


        FirebaseFirestore fb = FirebaseFirestore.getInstance();

        fb.collection("Games").document(gameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    gameRoom = task.getResult().toObject(GameRoom.class);
                    displayRecyclerView();

                }
            }
        });
        ArrayList<RatingArea> ratingAreas = new ArrayList<>();





        // getting the recycler view from xml
    //    recyclerViewAreas = (RecyclerView)findViewById(R.id.recyclerView_ratingArea);
      //  recyclerViewAreas.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // initializing the ratingAreasList
        ratingAreasList = new ArrayList<>();


        // the views I have in each rating area

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img);

        //binding MainActivity.java with activity_main.xml file
       //rtBar = (RatingBar) findViewById(R.id.ratingBar);
        //finding the specific RatingBar with its unique ID
       // LayerDrawable stars=(LayerDrawable)rtBar.getProgressDrawable();
        //Use for changing the color of RatingBar
     //   stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);


        //RatingArea ra1 = new RatingArea(bitmap, "username", 0);
       // RatingArea ra1 = new RatingArea("hi", "username", 0);




        //phase 2 - add to array list
    //    ratingAreasList = new ArrayList<RatingArea>();
   //     ratingAreasList.add(ra1);
        //creating recyclerview adapter

    }

    private void displayRecyclerView() {
        // game room
        // array list ratings


        for (int i = 0; i < gameRoom.getPlayersNames().size(); i++) {
            RatingArea ra = new RatingArea(gameRoom.getPlayersNames().get(i) + gameId,gameRoom.getPlayersNames().get(i),0.0f);

            ratingAreasList.add(ra);
        }

        recyclerViewAreas = (RecyclerView)findViewById(R.id.recyclerView_ratingArea);
        recyclerViewAreas.setHasFixedSize(true);
        recyclerViewAreas.setLayoutManager(new LinearLayoutManager(this));
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