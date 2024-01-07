package com.example.myproject1;

public class User {
    private String email;
    private String password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void SetEmail(String email){ this.email = email; }
    public void SetPassword(String password){ this.password = password; }
    public String GetEmail(){ return this.email; }
    public String GetPassword(){ return this.password; }



// TODO: Can I make a unique name in an efficient way? without going through all the usernames?

}
