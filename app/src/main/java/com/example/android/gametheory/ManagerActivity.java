package com.example.android.gametheory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ManagerActivity extends AppCompatActivity {
    private static final String TAG = ".ManagerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

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
                return new GameManageFragment();
            default:
                Log.d(TAG, "Unhandle case");
                return null;
        }
    }
}
