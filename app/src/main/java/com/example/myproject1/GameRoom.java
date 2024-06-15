package com.example.myproject1;

import com.example.myproject1.loginsignup_screen.Subjects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameRoom
{
    private String hostId;
    private  String subject;
    private  boolean started; // has the game started? T- yes, F- no
    private  int countUsersFinishedRating; // count how many users finished rating all drawings (to know when everyone has finished);
    private ArrayList<String> playersId;
    private ArrayList<String> playersNames;
    private ArrayList<Float> playersScoresList;

    public GameRoom(){}

    public GameRoom(String id, String username)
    {
        this.playersId = new ArrayList<>();
        this.playersId.add(id);
        this.playersNames = new ArrayList<>();
        this.playersNames.add(username);
        this.playersScoresList = new ArrayList<>();
        this.playersScoresList.add(0F);
        this.subject = null;
        this.hostId = id;
        //this.subject = randomSubject();
        this.started = false;
        this.countUsersFinishedRating = 0;
    }


    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean getStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getCountUsersFinishedRating() { return countUsersFinishedRating; }
    public void setCountUsersFinishedRating(int countUsersFinishedRating) {this.countUsersFinishedRating = countUsersFinishedRating;}

    public ArrayList<String> getPlayersId(){ return this.playersId; }
    public void setPlayersId(ArrayList<String> playersId){ this.playersId = playersId; }

    public ArrayList<String> getPlayersNames(){ return this.playersNames; }
    public void setPlayersNames(ArrayList<String> playersNames){ this.playersNames = playersNames; }

    public ArrayList<Float> getPlayersScoresList() { return playersScoresList; }
    public void setPlayersScoresList(ArrayList<Float> playersScoresList) { this.playersScoresList = playersScoresList; }

}
