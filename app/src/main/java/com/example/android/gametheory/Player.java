package com.example.android.gametheory;

public class Player {
    String uid, name;

    public Player(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() { return uid; }
    public String getName() { return name; }
}
