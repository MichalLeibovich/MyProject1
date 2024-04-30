package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

    public void finishedRating(View view)
    {
      //  for (int i = 0; i < ratingAreasList.size(); i++)
       // {
          //  float currentRaRate = ratingAreasList.get(i).getRating();
            sumRatingInGameRoom();
        //}


        // update all players rating in the firebase
        // rating area list holds all of ratings I scored

    }



    public void sumRatingInGameRoom()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value!=null && value.exists())
                {
                    // Convert the Firestore document to a GameRoom object
                    GameRoom gr = value.toObject(GameRoom.class);
                    // Retrieve the playersScoresList from the GameRoom object
                    ArrayList<Float> playersScoresList = gr.getPlayersScoresList();


                    for (int i = 0; i < ratingAreasList.size(); i++) {
                        playersScoresList.set(i,playersScoresList.get(i)+ratingAreasList.get(i).getRating());
                    }
                    // Update the value of the specific element in the ArrayList
                   // playersScoresList.set(index, playersScoresList.get(index) + rating);
//
                    // Update the modified playersScoresList in the Firestore document
                    gameRef.update("playersScoresList", playersScoresList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("RatingScreenActivity", "playersScoresList updated successfully");
                                    // I know the ratings got updated and now I can lock the button
                                    Button finishedButton = findViewById(R.id.b_finishedRating);
                                    finishedButton.setEnabled(false);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("RatingScreenActivity", "Error updating playersScoresList: " + e.getMessage());
                                }
                            });

                    int usersFinishedRating = gr.getCountUsersFinishedRating();
                    gameRef.update("countUsersFinishedRating", usersFinishedRating+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d( "RatingScreenActivity", "countUsersFinishedRating updated successfully");
                                    //TODO
                                    // if usersFinishedRating == gr.getNumOfUsers() --> move to next activity
                                    //int numOfUsers = gr.getNumOfUsers();
                                    int numOfUsers = gr.getPlayersNames().size();
                                    if (numOfUsers == usersFinishedRating+1)
                                    {
                                        Intent intent = new Intent(RatingScreenActivity.this, ResultsScreenActivity.class);
                                        intent.putExtra("gameId", gameId); // Pass the game code as an extra
                                        startActivity(intent);
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("RatingScreenActivity", "Error updating countUsersFinishedRating: " + e.getMessage());
                                }
                            });

                }
                else
                {
                    Log.d("RatingScreenActivity", "Game document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("RatingScreenActivity", "Error fetching game document: " + e.getMessage());
            }
        });
    }





//    public void Call(View v)
//    {
//        // This function is called when button is clicked.
//        // Display ratings, which is required to be converted into string first.
//        TextView t = (TextView)findViewById(R.id.textView2);
//        t.setText("You Rated :"+String.valueOf(rt.getRating()));
//    }






}