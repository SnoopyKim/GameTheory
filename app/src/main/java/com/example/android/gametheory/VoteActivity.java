package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoteActivity extends AppCompatActivity {
    private static final String TAG = ".VoteActivity";

    FirebaseUser user;
    DatabaseReference userRef;

    RecyclerView playerView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Player> playerList = new ArrayList<>();
    PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                        String playerUid = userData.getKey();
                        boolean playerStatus = (boolean) userData.child("status").getValue();
                        if (playerUid.equals(user.getUid()) || !playerStatus) continue;
                        String playerProfile = userData.child("profile").getValue() == null ? null : userData.child("profile").getValue().toString();
                        String playerName = userData.child("name").getValue().toString();
                        String playerNote = userData.child("note").getValue().toString();

                        //playerList.add(new Player(playerUid, playerProfile, playerName, playerNote, playerStatus));
                    }
                    playerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        playerView = findViewById(R.id.rv_player);
        playerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        playerView.setLayoutManager(layoutManager);

        playerAdapter = new PlayerAdapter(this, playerList, true);
        playerView.setAdapter(playerAdapter);

    }
}
