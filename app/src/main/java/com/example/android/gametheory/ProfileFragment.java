package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ".ProfileFragment";

    FirebaseUser user;
    DatabaseReference userRef;

    RecyclerView playerView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Player> playerList = new ArrayList<>();
    PlayerAdapter playerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                        String playerUid = userData.getKey();
                        if (playerUid.equals(user.getUid())) continue;
                        String playerName = userData.child("name").getValue().toString();

                        playerList.add(new Player(null,playerUid, playerName, getString(R.string.default_value_none), true));
                    }
                    playerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        playerView = v.findViewById(R.id.rv_player);
        playerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        playerView.setLayoutManager(layoutManager);

        playerAdapter = new PlayerAdapter(getActivity(), playerList);
        playerView.setAdapter(playerAdapter);

        // Inflate the layout for this fragment
        return v;
    }

}
