package com.example.android.gametheory;

public class Rank {
    private String name;
    private int score;

    public Rank(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
