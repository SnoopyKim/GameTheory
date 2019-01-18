package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class ManagerActivity extends AppCompatActivity {
    private static final String TAG = ".ManagerActivity";

    DatabaseReference gameRef;
    int currentGame = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        FirebaseMessaging.getInstance().subscribeToTopic("timer").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) { Log.d("subscribeToTopic", "success"); }
                else { Log.d("subscribeToTopic", "failed"); }
            }
        });

        gameRef = FirebaseDatabase.getInstance().getReference("game").child("now");
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentGame = ((Long)dataSnapshot.getValue()).intValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        fragmentReplace(0);

        Button btnProfile = findViewById(R.id.tab_player);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentReplace(0);
            }
        });
        Button btnGame = findViewById(R.id.tab_game);
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentReplace(1);
            }
        });
        Button btnGameChange = findViewById(R.id.tab_game_change);
        btnGameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentReplace(2);
            }
        });
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
        Fragment newFragment = getFragment(reqNewFragmentIndex);
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag, newFragment);
        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        switch (idx) {
            case 0:
                return new ProfileFragment();
            case 1:
                switch (currentGame) {
                    case 0:
                        return new GameMafiaManageFragment();
                    case 1:
                        return new GameBlockManageFragment();
                    default:
                        return null;
                }
            case 2:
                return new GameChangeFragment();
            default:
                Log.d(TAG, "Unhandle case");
                return null;
        }
    }
}
