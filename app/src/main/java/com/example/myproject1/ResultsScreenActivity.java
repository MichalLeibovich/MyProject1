package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsScreenActivity extends AppCompatActivity {

    String gameId;

    //a list to store all the players' names
    List<String> playersNamesInOrder;
    //the recyclerview
    RecyclerView recyclerView;
    int pointsInLevel = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);

        gameId = getIntent().getStringExtra("gameId");

        //getting the recyclerview from xml
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_results);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the playersNamesList
        playersNamesInOrder = new ArrayList<>();

       sortToRanks();

    }

    public void sortToRanks()
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
                    // Retrieve the playersNames and playersScoresList from the GameRoom object
                    ArrayList<String> playersNamesGr = gr.getPlayersNames();
                    ArrayList<Float> playersScoresGr = gr.getPlayersScoresList();
                    ArrayList<String> playersIdsGr = gr.getPlayersId();


                    ArrayList<Player> players = new ArrayList<>();
                    for(int i = 0; i < playersNamesGr.size(); i++)
                    {
                        Player p = new Player(playersScoresGr.get(i), playersNamesGr.get(i), playersIdsGr.get(i));
                        players.add(p);
                    }

                    Log.d("Before Sorting Algo", "onSuccess: " + players);

                    Collections.sort(players);

                    Log.d("After Sorting Algo", "onSuccess: " + players);
                    setPlayersNames(players, gr);

                }
                else
                {
                    Log.d("ResultsScreenActivity", "Game document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ResultsScreenActivity", "Error fetching game document: " + e.getMessage());
            }
        });
    }



    public void setPlayersNames(ArrayList<Player> players, GameRoom gr)
    {
        int numOfPlayers = players.size();
        TextView tvFirst = findViewById(R.id.tv_firstPlace);
        TextView tvSecond = findViewById(R.id.tv_secondPlace);
        TextView tvThird = findViewById(R.id.tv_thirdPlace);

        ImageView ivFirst = findViewById(R.id.iv_firstDrawing);
        ImageView ivSecond = findViewById(R.id.iv_secondDrawing);
        ImageView ivThird= findViewById(R.id.iv_thirdDrawing);

        TextView tvFirstScore = findViewById(R.id.tv_firstScore);
        TextView tvSecondScore = findViewById(R.id.tv_secondScore);
        TextView tvThirdScore = findViewById(R.id.tv_thirdScore);

        String firstName = players.get(0).getName();
        tvFirst.setText(firstName);
        setWinnersDrawings(firstName, ivFirst);
        float firstScore = players.get(0).getScore();
        tvFirstScore.setText(firstScore + "⭐");
        setPlayersPoints(players.get(0), numOfPlayers, 1);

        if(numOfPlayers >= 2) {
            String secondName = players.get(1).getName();
            tvSecond.setText(secondName);
            setWinnersDrawings(secondName, ivSecond);
            float secondScore =  players.get(1).getScore();
            tvSecondScore.setText(secondScore + "⭐");
            setPlayersPoints(players.get(1), numOfPlayers, 2);

        }
        if (numOfPlayers >= 3)
        {
            String thirdName =  players.get(2).getName();
            tvThird.setText(thirdName);
            setWinnersDrawings(thirdName, ivThird);
            float thirdScore = players.get(2).getScore();
            tvThirdScore.setText(thirdScore + "⭐");
            setPlayersPoints(players.get(2), numOfPlayers, 3);

        }

        if (numOfPlayers > 3)
        {
            for(int i = 3; i < numOfPlayers; i++)
            {
                // if I set 3 people, the 4th will be index 3. and so on
                String playerName = players.get(i).getName();
                float playerScore = players.get(i).getScore();
                playersNamesInOrder.add("#" + (i+1) + " " + playerName + " (" + playerScore + "⭐)");
                setPlayersPoints(players.get(i), numOfPlayers, i);
            }
            //creating recyclerview adapter
            PlayersNamesAdapter adapter = new PlayersNamesAdapter(ResultsScreenActivity.this, playersNamesInOrder);
            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }

    }




    public void setWinnersDrawings(String username, ImageView iv)
    {
        String strBitmap = username + gameId;
        // get the image from the firebase storage
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imageRef = storageRef.child(strBitmap + ".png");

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                Log.d("FIREBASE", "setting image from storage failed " + exception.getMessage());
            }
        });
    }


    public void setPlayersPoints(Player player, int numOfPlayers, int place)
    {
        TextView tvPoints = findViewById(R.id.tv_points);
        int newPointsToLevel = (numOfPlayers + 1) * 10 - place * 10;
        String id = player.getId();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        if(id.equals(userId))
        {
            tvPoints.setText("Well done! You get " + newPointsToLevel + " points");
            addPointsToPointsInLevel(newPointsToLevel, id);
        }
    }


    public void addPointsToPointsInLevel(int newPointsToLevel, String userId)
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value!=null && value.exists())
                {
                    User user = value.toObject(User.class);
                    pointsInLevel = user.getPointsInLevel();
                    pointsInLevel += newPointsToLevel;
                    userRef.update("pointsInLevel", pointsInLevel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("ResultsScreenActivity", "pointsInLevel updated successfully");
                                    checkLevelUpgrading(user, userRef, pointsInLevel);
                                    addGameToGameIds(user, userRef);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("ResultsScreenActivity", "Error updating pointsInLevel: " + e.getMessage());
                                }
                            });
                }
                else
                {
                    Log.d("ResultsScreenActivity", "User document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ResultsScreenActivity", "Error fetching user document: " + e.getMessage());
            }
        });
    }

    public void checkLevelUpgrading(User user, DocumentReference userRef, int pointsInLevel)
    {
        int level = user.getLevel();
        int levelPoints = level * 100;
        if (pointsInLevel >= levelPoints)
        {
            int difference = pointsInLevel - levelPoints;
            upgradeLevel(userRef, level, difference);
        }
    }

    public void upgradeLevel(DocumentReference userRef, int level, int diff)
    {
        int newlevel = level + 1;
        userRef.update("level", newlevel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ResultsScreenActivity", "pointsInLevel updated successfully");
                        resetPointsInLevel(userRef, diff);
                        upgradeLevelDialog(newlevel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ResultsScreenActivity", "Error updating pointsInLevel: " + e.getMessage());
                    }
                });
    }

    public void resetPointsInLevel(DocumentReference userRef, int diff)
    {
        userRef.update("pointsInLevel", diff)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ResultsScreenActivity", "pointsInLevel updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ResultsScreenActivity", "Error updating pointsInLevel: " + e.getMessage());
                    }
                });
    }

    public void upgradeLevelDialog(int level)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box_newlevel);
        dialog.setCancelable(false);
        TextView tv = dialog.findViewById(R.id.tv_leveLContent);
        tv.setText("Congratulations! You just got upgraded! You are now level " + level + "! 🎉");
        Button btn = dialog.findViewById(R.id.btn_newlevelYay);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void addGameToGameIds(User user, DocumentReference userRef)
    {
        ArrayList<String> newGameIds = user.getGameIdsList();
        newGameIds.add(gameId);
        userRef.update("gameIdsList", newGameIds);
    }

    public void backHomeClicked(View view)
    {
        Intent intent = new Intent(ResultsScreenActivity.this, MainScreenActivity.class);
        startActivity(intent);
    }



}