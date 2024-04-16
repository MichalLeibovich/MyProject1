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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.GameScreen.GameActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {

    String gameId;
    String hostId;

    //a list to store all the players' names
    List<String> playersNamesList;
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

        String player = getIntent().getStringExtra("player");

        if(player.equals("player"))
             addCurrentPlayerToGame(gameId);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewWaitingRoom);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the playersNamesList
        playersNamesList = new ArrayList<>();

        displayStartToHost1();



        // listen for changes in the gameroom
        fb.collection("Games").document(gameId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null && value.exists())
                {
                    // check if game has started or if another user joined

                    GameRoom gr = value.toObject(GameRoom.class);

                    // if game hasn't started
                    if(!gr.getStarted())
                    {
                       // gr.getPlayersNames().get(gr.getPlayersNames().size()-1);
                        playersNamesList.clear();
                        playersNamesList.addAll(gr.getPlayersNames());
                        //creating recyclerview adapter
                        PlayersNamesAdapter adapter = new PlayersNamesAdapter(WaitingRoomActivity.this, playersNamesList);
                        //setting adapter to recyclerview
                        recyclerView.setAdapter(adapter);
                    }
                    // if game has started, start to everybody
                    else
                    {
                        // everybody moves to next activity
                        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
                        intent.putExtra("gameId", gameId); // Pass the game code as an extra
                        startActivity(intent);
                    }
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Closing Activity")
//                .setMessage("Are you sure you want to close this activity?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }



    private void addCurrentPlayerToGame(String gameId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);

        //gameRef.update("playersNames", FieldValue.arrayUnion(MainScreenActivity.username));
        gameRef.update("playersNames", FieldValue.arrayUnion(HomeFragment.username));

        addUserToScores();

        addNumOfUsers();
    }


    public void addNumOfUsers()
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
                    int updatedNumOfUsers = gr.getNumOfUsers() + 1;

                    // Update the modified numOfUsers in the Firestore document
                    gameRef.update("numOfUsers", updatedNumOfUsers)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("WaitingRoomActivity", "playersScoresList updated successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("WaitingRoomActivity", "Error updating playersScoresList: " + e.getMessage());
                                }
                            });
                }
                else
                {
                    Log.d("WaitingRoomActivity", "Game document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("WaitingRoomActivity", "Error fetching game document: " + e.getMessage());
            }
        });
    }

    public void addUserToScores()
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
                    ArrayList<Float> newScoresList = gr.getPlayersScoresList();
                    newScoresList.add(0F);

                    // Update the modified playersScoresList in the Firestore document
                    gameRef.update("playersScoresList", newScoresList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("WaitingRoomActivity", "playersScoresList updated successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("WaitingRoomActivity", "Error updating playersScoresList: " + e.getMessage());
                                }
                            });
                }
                else
                {
                    Log.d("WaitingRoomActivity", "Game document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("WaitingRoomActivity", "Error fetching game document: " + e.getMessage());
            }
        });
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
//                        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
//                        intent.putExtra("gameId", gameId); // Pass the game code as an extra
//                        startActivity(intent);
                        Log.e("FIREBASE", "Game has started");
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


    public void displayStartToHost1()
    {
        // current user's id
        FirebaseAuth fUser = FirebaseAuth.getInstance();
        String currentUserId = fUser.getCurrentUser().getUid();

        // host's id
        hostId = "";
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    hostId = documentSnapshot.getString("hostId");
                    displayStartToHost2(currentUserId, hostId);
                }
                else
                {
                    Log.d("WaitingRoomActivity", "Game document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.e("MainScreenActivity", "Error fetching user document: " + e.getMessage());
            }
        });
    }

    public void displayStartToHost2(String currentUserId, String hostId)
    {
        Button startButton = findViewById(R.id.startGameButton);

        if(currentUserId.equals(hostId))
        {
            startButton.setEnabled(true);
        }
        else
        {
            startButton.setEnabled(false);
        }
    }


    public void shareCode(View view)
    {
        // impilicit intent - אינטנט מרומז
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //this action indicates that you want to send data
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, gameId);
        startActivityForResult(Intent.createChooser(shareIntent, "share using"), 1);
    }
}