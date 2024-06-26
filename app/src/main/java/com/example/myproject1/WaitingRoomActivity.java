package com.example.myproject1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {

    public static String gameId;
    //String gameId;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the playersNamesList
        playersNamesList = new ArrayList<>();

        displayStartToHost1();



        // listen for changes in the gameroom
        listenForChanges();

    }


    public void listenForChanges()
    {
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
                        String subject = gr.getSubject();
                        // everybody moves to next activity
                        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
                        intent.putExtra("gameId", gameId); // Pass the game code as an extra
                        intent.putExtra("subject", subject);
                        startActivity(intent);
                    }
                }
            }
        });
    }



    private void addCurrentPlayerToGame(String gameId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);

        //gameRef.update("playersNames", FieldValue.arrayUnion(MainScreenActivity.username));
        gameRef.update("playersNames", FieldValue.arrayUnion(HomeFragment.username));

        addUserToScores();

        addUserIdToPlayersId();
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


    public void addUserIdToPlayersId()
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
                    ArrayList<String> newIdsList = gr.getPlayersId();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = currentUser.getUid();
                    newIdsList.add(userId);

                    // Update playersId in the Firestore document
                    gameRef.update("playersId", newIdsList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("WaitingRoomActivity", "playersId updated successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("WaitingRoomActivity", "Error updating playersId: " + e.getMessage());
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


        if(playersNamesList!=null && playersNamesList.size()<2)
        {
            Toast.makeText(this,"please wait for players to join",Toast.LENGTH_SHORT).show();
            return;
        }
        // Update the "started" field to true in the game document
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);

        gameRef
                .update("started", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Game started successfully
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
        // In order to display start to host only after I fetched the hostId in this GameRoom,
        // I separated this func to 2 functions.

        // this one as said fetches the hostId, then calls for displayStartToHost2


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
                Log.e("WaitingRoomActivity", "Error fetching GameRoom document: " + e.getMessage());
            }
        });
    }

    public void displayStartToHost2(String currentUserId, String hostId)
    {
        // after I have the hostId, if the current user is the host, enable start button. If it's not, disable.
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
        // Implicit intents do not name a specific component like explicit intent, -->
        // instead declare general action to perform, which allows a component from another app to handle

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //this action indicates that you want to send data
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, gameId);
        //startActivityForResult(Intent.createChooser(shareIntent, "share using"), 1);
        Intent chooser = Intent.createChooser(shareIntent, "share using");
        mActivityResultLauncher.launch(chooser);
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    Log.d("WaitingRoomActivity", "Returned from sharing intent");
                }
            }
    );

}