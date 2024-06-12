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

    ArrayList<Integer> ISortedList;

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
        ISortedList = new ArrayList<Integer>();
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


                    ///
                    ArrayList<Player> players = new ArrayList<>();
                    for(int i = 0; i < playersNamesGr.size(); i++)
                    {
                        Player p = new Player(playersScoresGr.get(i), playersNamesGr.get(i), playersIdsGr.get(i));
                        players.add(p);
                    }

                    Log.d("Before Sorting Algo", "onSuccess: " + players);

                    Collections.sort(players);

                    Log.d("After Sorting Algo", "onSuccess: " + players);
                    setPlayersNamesOption(players, gr);
/*




                    //
                    // Retrieve the numOfUsers from the GameRoom object
                    while (ISortedList.size() < playersNamesGr.size())
                    {
                        int indexOfMax = indexOfMax(playersScoresGr);
                        playersScoresGr.remove(indexOfMax);
                        ISortedList.add(indexOfMax);
                    }

                    setPlayersNames(ISortedList);

 */

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



    public int indexOfMax(ArrayList<Float> rates)
    {
        float maxRate = 0;
        int maxI = 0;
        for (int i = 0; i < rates.size(); i++)
        {
            if (rates.get(i) > maxRate)
            {
                maxRate = rates.get(i);
                maxI = i;
            }
        }
        return maxI;
    }



    public void setPlayersNamesOption(ArrayList<Player> players,GameRoom gr)
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
        //setPlayersPoints(gr, numOfPlayers, 1);
        setPlayersPointsOption(players.get(0), numOfPlayers, 1);

        if(numOfPlayers >= 2) {
            String secondName = players.get(1).getName();
            tvSecond.setText(secondName);
            setWinnersDrawings(secondName, ivSecond);
            float secondScore =  players.get(1).getScore();
            tvSecondScore.setText(secondScore + "⭐");
            //setPlayersPoints(gr, numOfPlayers, 2);
            setPlayersPointsOption(players.get(1), numOfPlayers, 2);

        }
        if (numOfPlayers >= 3)
        {
            String thirdName =  players.get(2).getName();
            tvThird.setText(thirdName);
            setWinnersDrawings(thirdName, ivThird);
            float thirdScore = players.get(2).getScore();
            tvThirdScore.setText(thirdScore + "⭐");
            //setPlayersPoints(gr, numOfPlayers, 3);
            setPlayersPointsOption(players.get(2), numOfPlayers, 3);

        }

        if (numOfPlayers > 3)
        {
            for(int i = 3; i < numOfPlayers; i++)
            {
                // if I set 3 people, the 4th will be index 3. and so on
                String playerName = players.get(i).getName();
                float playerScore = players.get(i).getScore();
                playersNamesInOrder.add("#" + (i+1) + " " + playerName + " (" + playerScore + "⭐)");
                //otherPlayersNames.add(newName);
                //setPlayersPoints(gr, numOfPlayers, i);
                setPlayersPointsOption(players.get(i), numOfPlayers, i);
            }
            //playersNamesInOrder.addAll(otherPlayersNames);
            //creating recyclerview adapter
            PlayersNamesAdapter adapter = new PlayersNamesAdapter(ResultsScreenActivity.this, playersNamesInOrder);
            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }
        // כביכול אני יכולה לעשות פעולה נפרדת אבל זה אותו גייםרום אז חבל לא להשתמש בזה ולקרוא הכל מחדש
        //setPlayersPoints(gr, numOfUsers);
    }








/*

    public void setPlayersNames(ArrayList<Integer> ISortedList)
    {
        TextView tvFirst = findViewById(R.id.tv_firstPlace);
        TextView tvSecond = findViewById(R.id.tv_secondPlace);
        TextView tvThird = findViewById(R.id.tv_thirdPlace);

        ImageView ivFirst = findViewById(R.id.iv_firstDrawing);
        ImageView ivSecond = findViewById(R.id.iv_secondDrawing);
        ImageView ivThird= findViewById(R.id.iv_thirdDrawing);

        TextView tvFirstScore = findViewById(R.id.tv_firstScore);
        TextView tvSecondScore = findViewById(R.id.tv_secondScore);
        TextView tvThirdScore = findViewById(R.id.tv_thirdScore);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value!=null && value.exists())
                {
                    // Convert the Firestore document to a GameRoom object
                    GameRoom gr = value.toObject(GameRoom.class);
                    saveDrawingInMyGallery(gr);
                    // Retrieve the numOfUsers from the GameRoom object
                    ArrayList<String> playersNamesGr = gr.getPlayersNames();
                    ArrayList<Float> playersScoresGr = gr.getPlayersScoresList();
                    int numOfUsers = playersNamesGr.size();
                    // I don't need numOfUsers on database, I can just take an array's size

                    String firstName = gr.getPlayersNames().get(ISortedList.get(0));
                    tvFirst.setText(firstName);
                    setWinnersDrawings(firstName, ivFirst);
                    float firstScore = playersScoresGr.get(ISortedList.get(0));
                    tvFirstScore.setText(firstScore + "⭐");
                    setPlayersPoints(gr, numOfUsers, 1);


                    if(numOfUsers>1) {
                        String secondName = gr.getPlayersNames().get(ISortedList.get(1));
                        tvSecond.setText(secondName);
                        setWinnersDrawings(secondName, ivSecond);
                        float secondScore = playersScoresGr.get(ISortedList.get(1));
                        tvSecondScore.setText(secondScore + "⭐");
                        setPlayersPoints(gr, numOfUsers, 2);

                    }
                    if (numOfUsers == 3)
                    {
                        String thirdName = gr.getPlayersNames().get(ISortedList.get(2));
                        tvThird.setText(thirdName);
                        setWinnersDrawings(thirdName, ivThird);
                        float thirdScore = playersScoresGr.get(ISortedList.get(2));
                        tvThirdScore.setText(thirdScore + "⭐");
                        setPlayersPoints(gr, numOfUsers, 3);
                    }
                    int playersNamesSet = 3;
                    if (numOfUsers > playersNamesSet)
                    {
                        ArrayList<String> otherPlayersNames = new ArrayList<>();
                        while(numOfUsers > playersNamesSet)
                        {
                            int requestedI = ISortedList.get(playersNamesSet); // if I set 3 people, the 4th will be index 3. and so on
                            String playerName = playersNamesGr.get(requestedI);
                            float playerScore = playersScoresGr.get(requestedI);
                            playersNamesInOrder.add("#" + requestedI + " " + playerName + " (" + playerScore + "⭐)");
                            //otherPlayersNames.add(newName);
                            setPlayersPoints(gr, numOfUsers, requestedI);
                            playersNamesSet++;
                        }
                        //playersNamesInOrder.addAll(otherPlayersNames);
                        //creating recyclerview adapter
                        PlayersNamesAdapter adapter = new PlayersNamesAdapter(ResultsScreenActivity.this, playersNamesInOrder);
                        //setting adapter to recyclerview
                        recyclerView.setAdapter(adapter);
                    }
                    // כביכול אני יכולה לעשות פעולה נפרדת אבל זה אותו גייםרום אז חבל לא להשתמש בזה ולקרוא הכל מחדש
                    //setPlayersPoints(gr, numOfUsers);
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

 */

    /*

    public void saveDrawingInMyGallery(GameRoom gr)
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value != null && value.exists())
                {
                    User user = value.toObject(User.class);
                    ArrayList<String> gameIdsList = user.getGameIdsList();
                    gameIdsList.add(gameId);
                    userRef.update("gameIdsList", gameIdsList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("ResultsScreenActivity", "User document got updated");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e){
                                    Log.d("ResultsScreenActivity", "User document got updated");
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ResultsScreenActivity", "User document isn't found");
            }
        });
//        String username = HomeFragment.username;
//        String drawingName = username + gameId;


    }

     */

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

    public void setPlayersPoints(GameRoom gr, int numOfPlayers, int place)
    {
        TextView tvPoints = findViewById(R.id.tv_points);
        int newPointsToLevel = (numOfPlayers + 1) * 10 - place * 10;
        String id = gr.getPlayersId().get(ISortedList.get(place - 1));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        if(id.equals(userId))
        {
            tvPoints.setText("Well done! You get " + newPointsToLevel + " points");
        }
        //int newPointsToLevel = numOfUsers * 10 - (place - 1) * 10;
        addPointsToPointsInLevel(newPointsToLevel, id);
    }

    public void setPlayersPointsOption(Player player, int numOfPlayers, int place)
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

    int pointsInLevel = 0;
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