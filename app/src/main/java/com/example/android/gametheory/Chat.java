package com.example.android.gametheory;

public class Chat {
    String uid;
    String text;

    public Chat(String uid, String text) {
        this.uid = uid;
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }
}
