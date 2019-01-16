package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimerDialog extends DialogFragment {


    public TimerDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_timer, container, false);

        DatabaseReference timerRef = FirebaseDatabase.getInstance().getReference("game").child("0").child("time");
        final TextView tvTimer = v.findViewById(R.id.tv_timer);
        timerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long time = (long)dataSnapshot.getValue();
                long minute = time/60;
                long second = (time%60);
                tvTimer.setText(String.format("%2d : %2d", minute,second));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return v;
    }

}
