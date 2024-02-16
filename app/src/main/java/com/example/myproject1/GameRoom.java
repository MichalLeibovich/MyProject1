package com.example.myproject1;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class GameRoom
{
    //ArrayList<User> users; do I need a friends list?
    String hostID;
    String subject;
    boolean started; // has the game started? T- yes, F- no
    int countUsersRanking; // count how many users finished ranking all drawings (to know when everyone has finished);
    int numOfUsers; // how many users are in here

    // TODO: func that randomizes a subject

    public GameRoom(){}

    public GameRoom(String user) {
        //this.users = null;
        this.hostID = user;
        //this.subject = randomSubject();
        this.started = false;
        this.numOfUsers = 1;
        this.countUsersRanking = 0;
    }
}
