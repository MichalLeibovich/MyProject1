package com.example.myproject1;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class GameRoom
{
    private String hostId;
    private  String subject;
    private  boolean started; // has the game started? T- yes, F- no
    private  int countUsersRanking; // count how many users finished ranking all drawings (to know when everyone has finished);
    private  int numOfUsers; // how many users are in here
    private ArrayList<String> playersId;
    private ArrayList<String> playersNames;


    // TODO: func that randomizes a subject

    public GameRoom(){}


    public GameRoom(FirebaseUser user) {
        this.players = new ArrayList<>();
        this.players.add(user);
        this.subject = null;
        this.hostId = user.getUid();
        //this.subject = randomSubject();
        this.started = false;
        this.numOfUsers = 1;
        this.countUsersRanking = 0;
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

    public int getCountUsersRanking() {
        return countUsersRanking;
    }
    public void setCountUsersRanking(int countUsersRanking) {this.countUsersRanking = countUsersRanking;}

    public int getNumOfUsers() {
        return numOfUsers;
    }
    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public ArrayList<FirebaseUser> getPlayers(){ return this.players; }
    public void setPlayers(ArrayList<FirebaseUser> players){ this.players = players; }

}
