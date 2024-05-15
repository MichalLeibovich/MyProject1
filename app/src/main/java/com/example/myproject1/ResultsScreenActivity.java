package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ResultsScreenActivity extends AppCompatActivity {

String gameId;

    //a list to store all the players' names
    List<String> playersNamesInOrder;
    //the recyclerview
    RecyclerView recyclerView;

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

    public ArrayList<Integer> sortToRanks()
    {
        ArrayList<Integer> sortedList = new ArrayList<Integer>();
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
                    // Retrieve the numOfUsers from the GameRoom object
                    while (sortedList.size() < playersNamesGr.size())
                    {
                        int indexOfMax = indexOfMax(playersScoresGr);
                        playersScoresGr.remove(indexOfMax);
                        sortedList.add(indexOfMax);
                    }

                    ArrayList<Integer> ISortedList = sortedList;
                    setPlayersNames(ISortedList);

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
        return sortedList;
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





    public void setPlayersNames(ArrayList<Integer> ISortedList)
    {
        TextView tvFirst = findViewById(R.id.tv_firstPlace);
        TextView tvSecond = findViewById(R.id.tv_secondPlace);
        TextView tvThird = findViewById(R.id.tv_thirdPlace);
        TextView tvPoints = findViewById(R.id.tv_points);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value!=null && value.exists())
                {
                    // Convert the Firestore document to a GameRoom object
                    GameRoom gr = value.toObject(GameRoom.class);
                    // Retrieve the numOfUsers from the GameRoom object
                    ArrayList<String> playersNamesGr = gr.getPlayersNames();
                    ArrayList<Float> playersScoresGr = gr.getPlayersScoresList();
                    int numOfUsers = playersNamesGr.size();
                    // I don't need numOfUsers on database, I can just take an array's size

                    String firstName = gr.getPlayersNames().get(ISortedList.get(0));
                    tvFirst.setText(firstName);
                    int newPointsToLevel = (numOfUsers + 1) * 10 - 1 * 10;
                    tvPoints.setText("Well done! You get " + newPointsToLevel + " points");
                    //todo
                    String firstId = gr.getPlayersId().get(ISortedList.get(0));
                    addPointsToPointsInLevel(newPointsToLevel, firstId);
                    //todo

                    if(numOfUsers>1) {
                        String secondName = gr.getPlayersNames().get(ISortedList.get(1));
                        tvSecond.setText(secondName);
                    }
                    if (numOfUsers == 3)
                    {
                        String thirdName = gr.getPlayersNames().get(ISortedList.get(2));
                        tvThird.setText(thirdName);
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
                            playersNamesInOrder.add("#" + requestedI + ": " + playerName + " (total rate: " + playerScore + ")");
                            //otherPlayersNames.add(newName);
                            playersNamesSet++;
                        }
                        //playersNamesInOrder.addAll(otherPlayersNames);
                        //creating recyclerview adapter
                        PlayersNamesAdapter adapter = new PlayersNamesAdapter(ResultsScreenActivity.this, playersNamesInOrder);
                        //setting adapter to recyclerview
                        recyclerView.setAdapter(adapter);
                    }
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


    public void addPointsToPointsInLevel(int newPointsToLevel, String userId)
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                if (value!=null && value.exists())
                {
                    // Convert the Firestore document to a User object
                    User user = value.toObject(User.class);
                    // Retrieve the pointsInLevel from the User object
                    int pointsInLevel = user.getPointsInLevel();
                    pointsInLevel += newPointsToLevel;

                    // Update pointsInLevel in the Firestore document
                    userRef.update("pointsInLevel", pointsInLevel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("ResultsScreenActivity", "pointsInLevel updated successfully");
                                    // Now that it's updated, I can edit the pointsInLevel in homeFragment. Or is it already updated?
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
}