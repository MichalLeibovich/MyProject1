package com.example.myproject1;

public class User {
    private String email;
    private String password;

    public User(){}

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password){ this.password = password; }
    public String getEmail(){ return this.email; }
    public String getPassword(){ return this.password; }



// TODO: Can I make a unique name in an efficient way? without going through all the usernames?

}
