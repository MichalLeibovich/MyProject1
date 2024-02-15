package com.example.myproject1;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class GameRoom
{
    //ArrayList<User> users; do I need a friends list?
    FirebaseUser host;
    String subject;
    boolean started; // has the game started? T- yes, F- no
    int countUsersRanking; // count how many users finished ranking all drawings (to know when everyone has finished);
    int numOfUsers; // how many users are in here

    // TODO: func that randomizes a subject

    public GameRoom(FirebaseUser user) {
        //this.users = null;
        this.host = user;
        //this.subject = randomSubject();
        this.started = false;
        this.numOfUsers = 1;
        this.countUsersRanking = 0;
    }
}
