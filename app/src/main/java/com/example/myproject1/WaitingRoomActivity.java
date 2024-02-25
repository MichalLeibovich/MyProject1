package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.GamePage.GameActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {

    String gameId;

    //a list to store all the products

    List<String> productList;
    //the recyclerview
    RecyclerView recyclerView;

    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        // Get the game code from the intent extras
        gameId = getIntent().getStringExtra("gameId");

        TextView textView = findViewById (R.id.codeTextView);
        textView.setText(gameId);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewWaitingRoom);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //initializing the productlist

        productList = new ArrayList<>();


        // listen for changes in the gameroom
        fb.collection("Games").document(gameId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null && value.exists())
                {
                    GameRoom gr = value.toObject(GameRoom.class);
                    productList.clear();
                    productList.addAll(gr.getPlayersNames());
                    //creating recyclerview adapter
                    PlayersNamesAdapter adapter = new PlayersNamesAdapter(WaitingRoomActivity.this, productList);

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);


                }
            }
        });



/*
        PlayerName pn1 = new PlayerName("hello");
        PlayerName pn2 = new PlayerName("hello");
        PlayerName pn3 = new PlayerName("hello");
        PlayerName pn4 = new PlayerName("hello");
        PlayerName pn5 = new PlayerName("hello");
        PlayerName pn6 = new PlayerName("hello");


        //phase 2 - add to array list

        productList = new ArrayList<PlayerName>();

        productList.add(pn1);
        productList.add(pn2);
        productList.add(pn3);
        productList.add(pn4);
        productList.add(pn5);
        productList.add(pn6);
*/

    }


    public void StartClicked(View view)
    {
        // Update the "started" field to true in the game document
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);

        gameRef
                .update("started", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Game started successfully
                        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to update document
                        Toast.makeText(WaitingRoomActivity.this, "Failed to start the game: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FIREBASE", "Failed to start the game: " + e.getMessage());
                    }
                });
    }


    public void shareCode(View view)
    {
        // impilicit intent - אינטנט מרומז
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //this action indicates taht you want to send data
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, let's play! Here's the game code: " + gameId);
        startActivityForResult(Intent.createChooser(shareIntent, "share using"), 1);
    }
}