package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("실시간 랭킹");

        playerRef = FirebaseDatabase.getInstance().getReference("game").child("1").child("players");
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    String stName = userData.child("name").getValue().toString();
                    int bronze = Integer.valueOf(userData.child("bronze").getValue().toString());
                    int silver = Integer.valueOf(userData.child("silver").getValue().toString());
                    int gold = Integer.valueOf(userData.child("gold").getValue().toString());

                    int score = bronze + 5*silver + 10*gold;

                    rankList.add(new Rank(stName, score));
                }
                Collections.sort(rankList, new Comparator<Rank>() {
                    @Override
                    public int compare(Rank rank1, Rank rank2) {
                        return String.valueOf(rank2.getScore()).compareTo(String.valueOf(rank1.getScore()));
                    }
                });
                rankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        rankView = findViewById(R.id.rv_rank);
        rankView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        rankView.setLayoutManager(layoutManager);

        rankAdapter = new RankAdapter(rankList,this);
        rankView.setAdapter(rankAdapter);
    }
}
