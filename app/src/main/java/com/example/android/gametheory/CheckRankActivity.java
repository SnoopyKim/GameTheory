package com.example.android.gametheory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CheckRankActivity extends AppCompatActivity {

    DatabaseReference playerRef;

    RecyclerView rankView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Rank> rankList = new ArrayList<>();
    RankAdapter rankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rank);

        playerRef = FirebaseDatabase.getInstance().getReference("game").child("1").child("players");

    }
}
