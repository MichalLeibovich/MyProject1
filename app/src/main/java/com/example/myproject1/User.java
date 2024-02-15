package com.example.myproject1;

public class User {
    private String email;
    private String password;

    // in a game:
    private double totalStars;
    private int rank;

    // the points you get:
    // ((participant + 1) * 10 - rank * 10
    //  (5 participants, 1nd rank): 6 * 10 - 1 * 10 = 60-10 = 50
    //  (5 participants, 2nd rank): 6 * 10 - 2 * 10 = 60-20 = 40
    //  (5 participants, 5nd rank): 6 * 10 - 5 * 10 = 60-50 = 10

    // user's level:
    private int pointsInLevel;
    private int level;


    public User(){}

    public User(String email, String password){
        this.email = email;
        this.password = password;
        this.totalStars = 0;
        this.rank = 0;
    }

    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password){ this.password = password; }
    public String getEmail(){ return this.email; }
    public String getPassword(){ return this.password; }



// TODO: Can I make a unique name in an efficient way? without going through all the usernames?

}
