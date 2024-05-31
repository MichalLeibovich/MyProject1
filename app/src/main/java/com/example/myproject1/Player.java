package com.example.myproject1;

public class Player implements Comparable<Player>{

    private float score;
    private String name;

    private String id;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Player(float score, String name, String id) {
        this.score = score;
        this.name = name;
        this.id = id;
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
        if(player.score > this.score)
            return 1;
        else return -1;
    }

    @Override
    public String toString() {
        return "Player{" +
                "score=" + this.score +
                ", name='" + this.name + '\'' +
                ", id='" + this.id + '\'' +
                '}';
    }
}
