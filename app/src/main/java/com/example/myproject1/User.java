package com.example.myproject1;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    String username;

    // user's level:
    private int pointsInLevel;
    private int level;

    private ArrayList<String> gameIdsList;


    public User(){}



    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String username){
        this.email = email;
        this.password = password;
        this.username = username;
        this.pointsInLevel = 0;
        this.level = 1;
        this.gameIdsList = new ArrayList<>();
    }

    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password){ this.password = password; }
    public String getEmail(){ return this.email; }
    public String getPassword(){ return this.password; }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}


    public int getPointsInLevel() {return pointsInLevel;}
    public void setPointsInLevel(int pointsInLevel) {this.pointsInLevel = pointsInLevel;}

    public int getLevel() {return level;}
    public void setLevel(int level) {this.level = level;}


    public ArrayList<String> getGameIdsList() { return gameIdsList;}

    public void setGameIdsList(ArrayList<String> gameIdsList) { this.gameIdsList = gameIdsList;}



}
