package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.myproject1.GameScreen.GameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class RatingScreenActivity extends AppCompatActivity {

    RatingBar rtBar;

    //a list to store all the rating areas
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


        ratingAreasList = new ArrayList<>();

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
                                    checkEveryoneFinished();
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


    public void checkEveryoneFinished()
    {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();

        fb.collection("Games").document(gameId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null && value.exists())
                {
                    // check if game has started or if another user joined

                    GameRoom gr = value.toObject(GameRoom.class);

                    // if game hasn't started
                    if(gr.getPlayersNames().size() == gr.getCountUsersFinishedRating())
                    {
                        // everybody moves to next activity
                        Intent intent = new Intent(RatingScreenActivity.this, ResultsScreenActivity.class);
                        intent.putExtra("gameId", gameId); // Pass the game code as an extra
                        startActivity(intent);
                    }
                }
            }
        });
    }



}