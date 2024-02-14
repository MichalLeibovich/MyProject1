package com.example.myproject1;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Game
{
    ArrayList<User> users;
    FirebaseUser host;
    String subject;
    boolean status; // has the game started? T- yes, F- no
    int count; // count how many users finished to rank all drawings (to know when everyone has finished);
    int numOfUsers; // how many users are in here
    int rank;

    // TODO: func that randomizes a subject

    public Game(FirebaseUser host) {
        this.users = null;
        this.host = host;
        //this.subject = randomSubject();
        this.status = false;
        this.numOfUsers = 1;
        this.count = 0;
        this.rank = 0;
    }
}
