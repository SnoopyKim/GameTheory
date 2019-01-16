package com.example.android.gametheory;

import java.io.Serializable;

public class Player implements Serializable {
    private String uid, profile, name, note;
    private boolean status;

    public Player(String uid, String profile, String name, String note, boolean status) {
        this.uid = uid;
        this.profile = profile;
        this.name = name;
        this.note = note;
        this.status = status;
    }

    public String getUid() { return uid; }
    public String getProfile() { return profile; }
    public String getName() { return name; }
    public String getNote() { return note; }
    public boolean isStatus() { return status; }
}
