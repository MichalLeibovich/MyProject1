package com.example.myproject1;

public class User {
    private String email;
    private String password;
    String username;


    // in a game:
    private double totalStars;
    private int rank;

    // user's level:

    private int pointsInLevel;
    private int level;

// the points you get:
    // ((participant + 1) * 10 - rank * 10
    //  (5 participants, 1nd rank): 6 * 10 - 1 * 10 = 60-10 = 50
    //  (5 participants, 2nd rank): 6 * 10 - 2 * 10 = 60-20 = 40
    //  (5 participants, 5nd rank): 6 * 10 - 5 * 10 = 60-50 = 10



    public User(){}



    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String username){
        this.email = email;
        this.password = password;
        this.username = username;
        this.totalStars = 0;
        this.rank = 0;
        this.pointsInLevel = 0;
        this.level = 0;
    }

    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password){ this.password = password; }
    public String getEmail(){ return this.email; }
    public String getPassword(){ return this.password; }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}



    public int getRank() {return rank;}
    public void setRank(int rank) {this.rank = rank;}

    public int getPointsInLevel() {return pointsInLevel;}
    public void setPointsInLevel(int pointsInLevel) {this.pointsInLevel = pointsInLevel;}

    public int getLevel() {return level;}
    public void setLevel(int level) {this.level = level;}

    public double getTotalStars() {return totalStars;}
    public void setTotalStars(double totalStars) {this.totalStars = totalStars;}





// TODO: Can I make a unique name in an efficient way? without going through all the usernames?

}
