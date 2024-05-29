package com.example.myproject1.loginsignup_screen;

public class Player implements Comparable<Player>{

    private float score;
    private String name;

    public Player(float score, String name) {
        this.score = score;
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Player player) {
        if(player.score < this.score)
            return 1;
        else return -1;
    }

    @Override
    public String toString() {
        return "Player{" +
                "score=" + score +
                ", name='" + name + '\'' +
                '}';
    }
}
