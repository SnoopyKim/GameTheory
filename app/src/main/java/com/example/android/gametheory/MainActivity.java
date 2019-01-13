package com.example.android.gametheory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = ".MainActivity";

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        fragmentReplace(0);

        Button btnProfile = findViewById(R.id.tab_profile);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                startActivity(intent);
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
                return new GameFragment();
            default:
                Log.d(TAG, "Unhandle case");
                return null;
        }
    }
}
