package com.example.android.gametheory;

public class Trade {
    private String uid, name;
    private int auction_id, bid;

    public Trade(String uid, String name, int aid, int bid) {
        this.uid = uid;
        this.name = name;
        this.auction_id = aid;
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getAuction_id() {
        return auction_id;
    }

    public int getBid() {
        return bid;
    }
}
