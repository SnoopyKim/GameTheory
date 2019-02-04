package com.example.android.gametheory;

public class Chat {
    String uid;
    String text, time;

    public Chat(String uid, String text, String time) {
        this.uid = uid;
        this.text = text;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }
}
