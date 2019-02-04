package com.example.android.gametheory;

import java.io.Serializable;

public class Player implements Serializable {
    private String uid, profile, name, note, job;
    private boolean status;
    private int age, money;

    public Player(String uid, String profile, String name, int age, String job, String note, boolean status) {
        this.uid = uid;
        this.profile = profile;
        this.name = name;
        this.age = age;
        this.job = job;
        this.note = note;
        this.status = status;
    }
    public Player(String uid, String profile, String name, String note, int money) {
        this.uid = uid;
        this.profile = profile;
        this.name = name;
        this.note = note;
        this.money = money;
    }

    public String getUid() { return uid; }
    public String getProfile() { return profile; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getJob() { return job; }
    public String getNote() { return note; }
    public boolean isStatus() { return status; }
}
