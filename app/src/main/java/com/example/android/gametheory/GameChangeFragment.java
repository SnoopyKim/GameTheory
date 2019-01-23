package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameChangeFragment extends Fragment {
    private static final String TAG = ".GameChangeFragment";

    DatabaseReference gameRef;
    DatabaseReference playerRef;

    ListView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_change, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("게임 변경");

        gameRef = FirebaseDatabase.getInstance().getReference("game");
        playerRef = FirebaseDatabase.getInstance().getReference("users");

        String [] titles = getResources().getStringArray(R.array.title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles);

        titleView = v.findViewById(R.id.lv_title);
        titleView.setAdapter(adapter);
        titleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                gameRef.child("now").setValue(position).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            setGame(position);
                        }
                    }
                });
            }
        });

        return v;
    }

    public void setGame(int id) {
        if (id==0) {
            gameRef.child("0").child("time").setValue(0);
            gameRef.child("0").child("vote").removeValue();
            gameRef.child("1").child("players").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        CustomUtils.displayToast(getContext(), "게임이 변경되었습니다.");
                        ManagerActivity activity = (ManagerActivity) getActivity();
                        activity.fragmentReplace(1);
                    }
                }
            });

        } else {
            gameRef.child("0").child("time").setValue(0);
            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                        boolean isAlive = (boolean)userData.child("status").getValue();
                        if (isAlive) {
                            HashMap<String, String> playerData = new HashMap<>();
                            playerData.put("name",userData.child("name").getValue().toString());
                            playerData.put("money", "10000");
                            playerData.put("bronze", "0");
                            playerData.put("silver", "0");
                            playerData.put("gold", "0");
                            gameRef.child("1").child("players").child(userData.getKey()).setValue(playerData);
                        }
                    }
                    CustomUtils.displayToast(getContext(), "게임이 변경되었습니다.");
                    ManagerActivity activity = (ManagerActivity) getActivity();
                    activity.fragmentReplace(1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        }
    }

}
