package com.example.android.gametheory;

public class Trade {
    private String uid, name, item;
    private int bid;

    public Trade(String uid, String name, String item, int bid) {
        this.uid = uid;
        this.name = name;
        this.item = item;
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getItem() {
        return item;
    }

    public int getBid() {
        return bid;
    }
}
